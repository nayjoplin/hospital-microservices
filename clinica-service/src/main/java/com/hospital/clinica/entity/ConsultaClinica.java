package com.hospital.clinica.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade que representa uma Consulta na Clínica
 */
@Entity
@Table(name = "consultas_clinica", indexes = {
    @Index(name = "idx_consulta_horario", columnList = "horario"),
    @Index(name = "idx_consulta_cpf", columnList = "cpf_paciente")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaClinica {

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

    @NotNull(message = "Horário é obrigatório")
    @Column(nullable = false)
    private LocalDateTime horario;

    @NotBlank(message = "Especialidade do médico é obrigatória")
    @Column(name = "especialidade_medico", nullable = false, length = 100)
    private String especialidadeMedico;

    @ManyToOne
    @JoinColumn(name = "medico_id")
    private Medico medico;

    @ElementCollection
    @CollectionTable(name = "consulta_sintomas", joinColumns = @JoinColumn(name = "consulta_id"))
    @Column(name = "sintoma")
    private List<String> sintomas = new ArrayList<>();

    @Column(name = "diagnostico", columnDefinition = "TEXT")
    private String diagnostico;

    @Column(name = "tratamento_sugerido", columnDefinition = "TEXT")
    private String tratamentoSugerido;

    @Column(name = "exame_solicitado_id")
    private Long exameSolicitadoId; // ID do exame criado no centro cirúrgico

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusConsulta status;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Column(name = "data_atendimento")
    private LocalDateTime dataAtendimento;

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        dataAtualizacao = LocalDateTime.now();
        if (status == null) {
            status = StatusConsulta.AGENDADA;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }

    /**
     * Enum para status da consulta
     */
    public enum StatusConsulta {
        AGENDADA,
        EM_ATENDIMENTO,
        ATENDIDA,
        CANCELADA
    }
}
