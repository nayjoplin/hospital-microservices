package com.hospital.agendamento.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    // restante dos campos conforme original
}
