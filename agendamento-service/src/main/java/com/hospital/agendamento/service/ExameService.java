package com.hospital.agendamento.service;

import com.hospital.agendamento.dto.ExameRequestDTO;
import com.hospital.agendamento.dto.ResponseDTO;
import com.hospital.agendamento.entity.Exame;
import com.hospital.agendamento.entity.Paciente;
import com.hospital.agendamento.exception.AgendamentoConflictException;
import com.hospital.agendamento.messaging.MessagePublisher;
import com.hospital.agendamento.repository.ExameRepository;
import com.hospital.agendamento.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Serviço para gerenciamento de agendamento de exames
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ExameService {

    private final ExameRepository exameRepository;
    private final PacienteRepository pacienteRepository;
    private final MessagePublisher messagePublisher;
    private final WebClient.Builder webClientBuilder;

    @Value("${service.centro-cirurgico.url}")
    private String centroCirurgicoServiceUrl;

    private static final DateTimeFormatter FORMATTER = 
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // Exames de alta complexidade
    private static final List<String> EXAMES_ALTA_COMPLEXIDADE = List.of(
        "ressonância magnética",
        "ressonancia magnetica",
        "tomografia",
        "cateterismo"
    );

    /**
     * Cadastra novo exame
     */
    @Transactional
    public ResponseDTO cadastrarExame(ExameRequestDTO request) {
        log.info("Iniciando cadastro de exame para paciente: {}", 
            request.getPaciente().getCpf());

        // 1. Validar e processar dados
        String cpf = request.getPaciente().getCpf();
        LocalDateTime horario = parseHorario(request.getHorario());
        String tipoExame = request.getExame();

        // 2. Verificar se paciente já tem agendamento no mesmo horário
        if (exameRepository.existsExameByPacienteAndHorario(cpf, horario)) {
            throw new AgendamentoConflictException(
                "O paciente já possui um exame agendado para este horário: " + 
                request.getHorario());
        }

        // 3. Salvar ou buscar paciente
        Paciente paciente = salvarOuBuscarPaciente(request.getPaciente());

        // 4. Verificar se é exame de alta complexidade
        boolean altaComplexidade = isExameAltaComplexidade(tipoExame);

        // 5. Verificar disponibilidade no centro cirúrgico
        verificarDisponibilidadeCentroCirurgico(horario, tipoExame);

        // 6. Criar exame local
        Exame exame = Exame.builder()
            .cpfPaciente(cpf)
            .nomePaciente(request.getPaciente().getNome())
            .horario(horario)
            .tipoExame(tipoExame)
            .status(Exame.StatusExame.AGENDADO)
            .tipo(altaComplexidade ? Exame.TipoExame.ALTA_COMPLEXIDADE : Exame.TipoExame.SIMPLES)
            .build();

        exame = exameRepository.save(exame);
        log.info("Exame salvo localmente com ID: {}", exame.getId());

        // 7. Enviar mensagem para o centro cirúrgico via RabbitMQ
        MessagePublisher.ExameMessage message = MessagePublisher.ExameMessage.builder()
            .exameId(exame.getId())
            .cpfPaciente(cpf)
            .nomePaciente(paciente.getNome())
            .horario(horario)
            .tipoExame(tipoExame)
            .acao("CRIAR")
            .build();

        messagePublisher.publicarExame(message);

        // 8. Retornar resposta
        return ResponseDTO.builder()
            .mensagem(String.format(
                "O %s de %s foi marcado para %s",
                "exame",
                paciente.getNome(),
                request.getHorario()
            ))
            .codigo("Aguardando confirmação do centro cirúrgico")
            .build();
    }

    /**
     * Busca exames por CPF do paciente
     */
    public List<Exame> buscarExamesPorCpf(String cpf) {
        log.info("Buscando exames para CPF: {}", cpf);
        return exameRepository.findByCpfPaciente(cpf);
    }

    /**
     * Busca exames por nome do paciente
     */
    public List<Exame> buscarExamesPorNome(String nome) {
        log.info("Buscando exames para nome: {}", nome);
        return exameRepository.findByNomePacienteContainingIgnoreCase(nome);
    }

    /**
     * Busca exames por tipo
     */
    public List<Exame> buscarExamesPorTipo(String tipo) {
        log.info("Buscando exames do tipo: {}", tipo);
        return exameRepository.findByTipoExameContainingIgnoreCase(tipo);
    }

    /**
     * Atualiza ID do centro cirúrgico no exame (chamado após receber confirmação)
     */
    @Transactional
    public void atualizarIdCentroCirurgico(Long exameId, Long idCentroCirurgico) {
        log.info("Atualizando exame {} com ID do centro cirúrgico: {}", 
            exameId, idCentroCirurgico);
        Exame exame = exameRepository.findById(exameId)
            .orElseThrow(() -> new RuntimeException("Exame não encontrado"));
        exame.setIdCentroCirurgico(idCentroCirurgico);
        exame.setStatus(Exame.StatusExame.CONFIRMADO);
        exameRepository.save(exame);
    }

    /**
     * Cancela um exame
     */
    @Transactional
    public void cancelarExame(Long exameId) {
        log.info("Cancelando exame: {}", exameId);
        Exame exame = exameRepository.findById(exameId)
            .orElseThrow(() -> new RuntimeException("Exame não encontrado"));
        
        exame.setStatus(Exame.StatusExame.CANCELADO);
        exameRepository.save(exame);

        // Notificar centro cirúrgico via mensageria
        MessagePublisher.ExameMessage message = MessagePublisher.ExameMessage.builder()
            .exameId(exameId)
            .cpfPaciente(exame.getCpfPaciente())
            .acao("CANCELAR")
            .build();
        
        messagePublisher.publicarExame(message);
    }

    /**
     * Verifica se o exame é de alta complexidade
     */
    private boolean isExameAltaComplexidade(String tipoExame) {
        String tipoLower = tipoExame.toLowerCase();
        return EXAMES_ALTA_COMPLEXIDADE.stream()
            .anyMatch(tipoLower::contains);
    }

    /**
     * Verifica disponibilidade no centro cirúrgico via HTTP
     */
    private void verificarDisponibilidadeCentroCirurgico(
            LocalDateTime horario, String tipoExame) {
        log.info("Verificando disponibilidade no centro cirúrgico para: {} - {}", 
            horario, tipoExame);
        
        WebClient webClient = webClientBuilder.baseUrl(centroCirurgicoServiceUrl).build();
        
        Map<String, Object> requestBody = Map.of(
            "horario", horario.format(FORMATTER),
            "tipoExame", tipoExame
        );

        try {
            Boolean disponivel = webClient.post()
                .uri("/api/procedimentos/verificar-disponibilidade")
                .body(Mono.just(requestBody), Map.class)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

            if (Boolean.FALSE.equals(disponivel)) {
                throw new AgendamentoConflictException(
                    "O " + tipoExame + " já possui angendamento no " + 
                    horario.format(FORMATTER));
            }
        } catch (AgendamentoConflictException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erro ao verificar disponibilidade no centro cirúrgico", e);
            // Continua o processo mesmo com erro de comunicação
        }
    }

    /**
     * Salva ou busca paciente existente
     */
    private Paciente salvarOuBuscarPaciente(ExameRequestDTO.PacienteDTO pacienteDTO) {
        return pacienteRepository.findByCpf(pacienteDTO.getCpf())
            .orElseGet(() -> {
                Paciente novoPaciente = Paciente.builder()
                    .nome(pacienteDTO.getNome())
                    .cpf(pacienteDTO.getCpf())
                    .idade(pacienteDTO.getIdade())
                    .sexo(pacienteDTO.getSexo())
                    .build();
                return pacienteRepository.save(novoPaciente);
            });
    }

    /**
     * Faz parse do horário do formato texto para LocalDateTime
     */
    private LocalDateTime parseHorario(String horarioStr) {
        try {
            return LocalDateTime.parse(horarioStr, FORMATTER);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                "Formato de horário inválido. Use: dd/MM/yyyy HH:mm");
        }
    }
}
