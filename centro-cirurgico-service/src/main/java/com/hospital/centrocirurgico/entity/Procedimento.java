package com.hospital.centrocirurgico.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidade que representa um Procedimento/Exame
 */
@Entity
@Table(name = "procedimentos", indexes = {
    @Index(name = "idx_proc_horario", columnList = "horario"),
    @Index(name = "idx_proc_cpf", columnList = "cpf_paciente")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Procedimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_agendamento")
    private Long idAgendamento; // ID do serviço de agendamento

    @NotBlank(message = "CPF do paciente é obrigatório")
    @Column(name = "cpf_paciente", nullable = false, length = 14)
    private String cpfPaciente;

    @NotBlank(message = "Nome do paciente é obrigatório")
    @Column(name = "nome_paciente", nullable = false, length = 200)
    private String nomePaciente;

    @Column(name = "horario")
    private LocalDateTime horario;

    @NotBlank(message = "Tipo de procedimento é obrigatório")
    @Column(name = "tipo_procedimento", nullable = false, length = 100)
    private String tipoProcedimento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoProcedimento tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PrioridadeProcedimento prioridade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusProcedimento status;

    @Column(name = "solicitado_por", length = 200)
    private String solicitadoPor;

    @Column(name = "resultado", columnDefinition = "TEXT")
    private String resultado;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Column(name = "data_realizacao")
    private LocalDateTime dataRealizacao;

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        dataAtualizacao = LocalDateTime.now();
        if (status == null) {
            status = StatusProcedimento.AGUARDANDO_AGENDAMENTO;
        }
        if (prioridade == null) {
            prioridade = PrioridadeProcedimento.PADRAO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }

    /**
     * Enum para tipo de procedimento
     */
    public enum TipoProcedimento {
        EXAME_SIMPLES,
        EXAME_ALTA_COMPLEXIDADE,
        CIRURGIA,
        PROCEDIMENTO_AMBULATORIAL
    }

    /**
     * Enum para prioridade
     */
    public enum PrioridadeProcedimento {
        BAIXA,
        PADRAO,
        ALTA,
        EMERGENCIAL
    }

    /**
     * Enum para status
     */
    public enum StatusProcedimento {
        AGUARDANDO_AGENDAMENTO,
        AGENDADO,
        EM_PREPARACAO,
        EM_ANDAMENTO,
        CONCLUIDO,
        CANCELADO
    }
}
