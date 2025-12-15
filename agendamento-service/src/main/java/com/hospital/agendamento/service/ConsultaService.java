package com.hospital.agendamento.service;

import com.hospital.agendamento.dto.ConsultaRequestDTO;
import com.hospital.agendamento.dto.ResponseDTO;
import com.hospital.agendamento.entity.Consulta;
import com.hospital.agendamento.entity.Paciente;
import com.hospital.agendamento.exception.AgendamentoConflictException;
import com.hospital.agendamento.messaging.MessagePublisher;
import com.hospital.agendamento.repository.ConsultaRepository;
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
 * Serviço para gerenciamento de agendamento de consultas
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;
    private final MessagePublisher messagePublisher;
    private final WebClient.Builder webClientBuilder;

    @Value("${service.clinica.url}")
    private String clinicaServiceUrl;

    private static final DateTimeFormatter FORMATTER = 
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Cadastra nova consulta
     */
    @Transactional
    public ResponseDTO cadastrarConsulta(ConsultaRequestDTO request) {
        log.info("Iniciando cadastro de consulta para paciente: {}", 
            request.getPaciente().getCpf());

        // 1. Validar e processar dados
        String cpf = request.getPaciente().getCpf();
        LocalDateTime horario = parseHorario(request.getHorario());

        // 2. Verificar se paciente já tem agendamento no mesmo horário
        if (consultaRepository.existsConsultaByPacienteAndHorario(cpf, horario)) {
            throw new AgendamentoConflictException(
                "O paciente já possui uma consulta agendada para este horário: " + 
                request.getHorario());
        }

        // 3. Salvar ou buscar paciente
        Paciente paciente = salvarOuBuscarPaciente(request.getPaciente());

        // 4. Verificar disponibilidade na clínica
        verificarDisponibilidadeClinica(horario, request.getMedico());

        // 5. Criar consulta local
        Consulta consulta = Consulta.builder()
            .cpfPaciente(cpf)
            .nomePaciente(request.getPaciente().getNome())
            .horario(horario)
            .especialidadeMedico(request.getMedico())
            .status(Consulta.StatusConsulta.AGENDADA)
            .build();

        consulta = consultaRepository.save(consulta);
        log.info("Consulta salva localmente com ID: {}", consulta.getId());

        // 6. Enviar mensagem para a clínica via RabbitMQ
        MessagePublisher.ConsultaMessage message = MessagePublisher.ConsultaMessage.builder()
            .consultaId(consulta.getId())
            .cpfPaciente(cpf)
            .nomePaciente(paciente.getNome())
            .horario(horario)
            .especialidadeMedico(request.getMedico())
            .acao("CRIAR")
            .build();

        messagePublisher.publicarConsulta(message);

        // 7. Retornar resposta
        return ResponseDTO.builder()
            .mensagem(String.format(
                "O %s de %s foi marcado para %s",
                "medico",
                paciente.getNome(),
                request.getHorario()
            ))
            .codigo("Aguardando confirmação da clínica")
            .build();
    }

    /**
     * Busca consultas por CPF do paciente
     */
    public List<Consulta> buscarConsultasPorCpf(String cpf) {
        log.info("Buscando consultas para CPF: {}", cpf);
        return consultaRepository.findByCpfPaciente(cpf);
    }

    /**
     * Busca consultas por nome do paciente
     */
    public List<Consulta> buscarConsultasPorNome(String nome) {
        log.info("Buscando consultas para nome: {}", nome);
        return consultaRepository.findByNomePacienteContainingIgnoreCase(nome);
    }

    /**
     * Atualiza ID da clínica na consulta (chamado após receber confirmação)
     */
    @Transactional
    public void atualizarIdClinica(Long consultaId, Long idClinica) {
        log.info("Atualizando consulta {} com ID da clínica: {}", consultaId, idClinica);
        Consulta consulta = consultaRepository.findById(consultaId)
            .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));
        consulta.setIdClinica(idClinica);
        consulta.setStatus(Consulta.StatusConsulta.CONFIRMADA);
        consultaRepository.save(consulta);
    }

    /**
     * Cancela uma consulta
     */
    @Transactional
    public void cancelarConsulta(Long consultaId) {
        log.info("Cancelando consulta: {}", consultaId);
        Consulta consulta = consultaRepository.findById(consultaId)
            .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));
        
        consulta.setStatus(Consulta.StatusConsulta.CANCELADA);
        consultaRepository.save(consulta);

        // Notificar clínica via mensageria
        MessagePublisher.ConsultaMessage message = MessagePublisher.ConsultaMessage.builder()
            .consultaId(consultaId)
            .cpfPaciente(consulta.getCpfPaciente())
            .acao("CANCELAR")
            .build();
        
        messagePublisher.publicarConsulta(message);
    }

    /**
     * Verifica disponibilidade na clínica via HTTP
     */
    private void verificarDisponibilidadeClinica(LocalDateTime horario, String especialidade) {
        log.info("Verificando disponibilidade na clínica para: {} - {}", horario, especialidade);
        
        WebClient webClient = webClientBuilder.baseUrl(clinicaServiceUrl).build();
        
        Map<String, Object> requestBody = Map.of(
            "horario", horario.format(FORMATTER),
            "especialidade", especialidade
        );

        try {
            Boolean disponivel = webClient.post()
                .uri("/api/clinica/verificar-disponibilidade")
                .body(Mono.just(requestBody), Map.class)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

            if (Boolean.FALSE.equals(disponivel)) {
                throw new AgendamentoConflictException(
                    "O " + especialidade + " já possui angendamento no " + 
                    horario.format(FORMATTER));
            }
        } catch (Exception e) {
            log.error("Erro ao verificar disponibilidade na clínica", e);
            // Continua o processo mesmo com erro de comunicação
            // Em produção, pode-se implementar circuit breaker
        }
    }

    /**
     * Salva ou busca paciente existente
     */
    private Paciente salvarOuBuscarPaciente(ConsultaRequestDTO.PacienteDTO pacienteDTO) {
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
