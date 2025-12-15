package com.hospital.centrocirurgico.service;

import com.hospital.centrocirurgico.dto.*;
import com.hospital.centrocirurgico.entity.Procedimento;
import com.hospital.centrocirurgico.repository.ProcedimentoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Serviço principal do Centro Cirúrgico
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CentroCirurgicoService {

    private final ProcedimentoRepository procedimentoRepository;

    private static final DateTimeFormatter FORMATTER = 
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // Lista de exames de alta complexidade
    private static final List<String> EXAMES_ALTA_COMPLEXIDADE = Arrays.asList(
        "ressonância magnética",
        "ressonancia magnetica",
        "tomografia",
        "tomografia computadorizada",
        "cateterismo",
        "endoscopia",
        "colonoscopia"
    );

    /**
     * Cria procedimento/exame a partir de mensagem do RabbitMQ
     */
    @Transactional
    public Long criarProcedimento(CriarExameDTO dto) {
        log.info("Criando procedimento para paciente: {}", dto.getCpfPaciente());

        LocalDateTime horario = dto.getHorario() != null ? 
            LocalDateTime.parse(dto.getHorario(), FORMATTER) : null;

        // Determinar tipo e prioridade
        boolean altaComplexidade = isExameAltaComplexidade(dto.getTipoExame());

        Procedimento procedimento = Procedimento.builder()
            .idAgendamento(dto.getExameId())
            .cpfPaciente(dto.getCpfPaciente())
            .nomePaciente(dto.getNomePaciente())
            .horario(horario)
            .tipoProcedimento(dto.getTipoExame())
            .tipo(altaComplexidade ? 
                Procedimento.TipoProcedimento.EXAME_ALTA_COMPLEXIDADE : 
                Procedimento.TipoProcedimento.EXAME_SIMPLES)
            .prioridade(Procedimento.PrioridadeProcedimento.PADRAO)
            .status(horario != null ? 
                Procedimento.StatusProcedimento.AGENDADO : 
                Procedimento.StatusProcedimento.AGUARDANDO_AGENDAMENTO)
            .build();

        procedimento = procedimentoRepository.save(procedimento);
        log.info("Procedimento criado com ID: {}", procedimento.getId());

        return procedimento.getId();
    }

    /**
     * Cria solicitação de exame (da clínica)
     */
    @Transactional
    public ResponseDTO criarSolicitacao(CriarSolicitacaoDTO dto) {
        log.info("Criando solicitação de exame: {}", dto);

        Procedimento.PrioridadeProcedimento prioridade = 
            Procedimento.PrioridadeProcedimento.valueOf(dto.getPrioridade().toUpperCase());

        boolean altaComplexidade = isExameAltaComplexidade(dto.getTipoExame());

        Procedimento procedimento = Procedimento.builder()
            .cpfPaciente(dto.getCpfPaciente())
            .nomePaciente(dto.getNomePaciente())
            .tipoProcedimento(dto.getTipoExame())
            .tipo(altaComplexidade ? 
                Procedimento.TipoProcedimento.EXAME_ALTA_COMPLEXIDADE : 
                Procedimento.TipoProcedimento.EXAME_SIMPLES)
            .prioridade(prioridade)
            .solicitadoPor(dto.getSolicitadoPor())
            .status(Procedimento.StatusProcedimento.AGUARDANDO_AGENDAMENTO)
            .build();

        procedimento = procedimentoRepository.save(procedimento);

        return ResponseDTO.builder()
            .mensagem("Exame registrado com sucesso")
            .codigoExame(procedimento.getId().toString())
            .build();
    }

    /**
     * Marca procedimento com horário
     */
    @Transactional
    public ResponseDTO marcarProcedimento(MarcarProcedimentoDTO dto) {
        log.info("Marcando procedimento: {}", dto);

        Long codigoExame = Long.parseLong(dto.getCodigoExame());
        LocalDateTime horario = LocalDateTime.parse(dto.getHorarioDesejado(), FORMATTER);

        // Buscar procedimento
        Procedimento procedimento = procedimentoRepository.findById(codigoExame)
            .orElseThrow(() -> new RuntimeException("Procedimento não encontrado"));

        // Verificar se CPF bate
        if (!procedimento.getCpfPaciente().equals(dto.getCpfPaciente())) {
            throw new RuntimeException("CPF não corresponde ao procedimento");
        }

        // Verificar disponibilidade
        if (!procedimento.getPrioridade().equals(Procedimento.PrioridadeProcedimento.EMERGENCIAL)) {
            Optional<Procedimento> conflito = procedimentoRepository
                .findByTipoAndHorario(procedimento.getTipoProcedimento(), horario);
            
            if (conflito.isPresent()) {
                throw new RuntimeException(
                    "Horário já ocupado para este tipo de procedimento");
            }
        }

        // Atualizar horário
        procedimento.setHorario(horario);
        procedimento.setStatus(Procedimento.StatusProcedimento.AGENDADO);
        procedimentoRepository.save(procedimento);

        return ResponseDTO.builder()
            .mensagem("Procedimento marcado com sucesso")
            .codigo(procedimento.getId().toString())
            .build();
    }

    /**
     * Verifica disponibilidade
     */
    public boolean verificarDisponibilidade(String horario, String tipoExame) {
        log.info("Verificando disponibilidade: {} - {}", horario, tipoExame);

        try {
            // Exames de alta complexidade só podem ser marcados com ID
            if (isExameAltaComplexidade(tipoExame)) {
                log.warn("Exame de alta complexidade só pode ser marcado com ID");
                return false;
            }

            LocalDateTime dateTime = LocalDateTime.parse(horario, FORMATTER);
            Optional<Procedimento> procedimento = procedimentoRepository
                .findByTipoAndHorario(tipoExame, dateTime);
            
            return procedimento.isEmpty();
        } catch (Exception e) {
            log.error("Erro ao verificar disponibilidade", e);
            return false;
        }
    }

    /**
     * Atualiza horário de procedimento existente
     */
    @Transactional
    public void atualizarHorario(Long procedimentoId, LocalDateTime novoHorario) {
        log.info("Atualizando horário do procedimento {}", procedimentoId);
        
        Procedimento procedimento = procedimentoRepository.findById(procedimentoId)
            .orElseThrow(() -> new RuntimeException("Procedimento não encontrado"));
        
        procedimento.setHorario(novoHorario);
        procedimento.setStatus(Procedimento.StatusProcedimento.AGENDADO);
        procedimentoRepository.save(procedimento);
    }

    /**
     * Busca procedimentos por CPF
     */
    public List<Procedimento> buscarPorCpf(String cpf) {
        return procedimentoRepository.findByCpfPaciente(cpf);
    }

    /**
     * Verifica se exame é de alta complexidade
     */
    private boolean isExameAltaComplexidade(String tipoExame) {
        String tipoLower = tipoExame.toLowerCase();
        return EXAMES_ALTA_COMPLEXIDADE.stream()
            .anyMatch(tipoLower::contains);
    }
}
