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
 * Entidade que representa um Exame
 */
@Entity
@Table(name = "exames", indexes = {
    @Index(name = "idx_exame_horario", columnList = "horario"),
    @Index(name = "idx_exame_cpf", columnList = "cpf_paciente")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Exame {

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

    @NotBlank(message = "Tipo de exame é obrigatório")
    @Column(name = "tipo_exame", nullable = false, length = 100)
    private String tipoExame;

    @Column(name = "id_centro_cirurgico")
    private Long idCentroCirurgico; // ID gerado no serviço de centro cirúrgico

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusExame status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoExame tipo;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        dataAtualizacao = LocalDateTime.now();
        if (status == null) {
            status = StatusExame.AGENDADO;
        }
        if (tipo == null) {
            tipo = TipoExame.SIMPLES;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }

    /**
     * Enum para status do exame
     */
    public enum StatusExame {
        AGENDADO,
        CONFIRMADO,
        REALIZADO,
        CANCELADO
    }

    /**
     * Enum para tipo de complexidade do exame
     */
    public enum TipoExame {
        SIMPLES,
        ALTA_COMPLEXIDADE
    }
}
