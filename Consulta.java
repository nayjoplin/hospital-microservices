package com.hospital.agendamento.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidade que representa uma Consulta médica
 */
@Entity
@Table(name = "consultas", indexes = {
    @Index(name = "idx_consulta_horario", columnList = "horario"),
    @Index(name = "idx_consulta_cpf", columnList = "cpf_paciente")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Column(name = "id_clinica")
    private Long idClinica; // ID gerado no serviço de clínica

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusConsulta status;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

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
        CONFIRMADA,
        REALIZADA,
        CANCELADA
    }
}
