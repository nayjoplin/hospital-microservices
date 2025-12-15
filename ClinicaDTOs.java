package com.hospital.clinica.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para atender consulta por horário
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AtenderConsultaHorarioDTO {

    @NotBlank(message = "CPF do paciente é obrigatório")
    @JsonProperty("CPF paciente")
    private String cpfPaciente;

    @NotBlank(message = "Horário é obrigatório")
    @JsonProperty("Horario")
    private String horario;

    @NotNull(message = "Sintomas são obrigatórios")
    @JsonProperty("Sintomas")
    private List<String> sintomas;
}

/**
 * DTO para atender consulta por código
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class AtenderConsultaCodigoDTO {

    @NotBlank(message = "CPF do paciente é obrigatório")
    @JsonProperty("CPF paciente")
    private String cpfPaciente;

    @NotBlank(message = "Código da consulta é obrigatório")
    @JsonProperty("Codigo consulta")
    private String codigoConsulta;

    @NotNull(message = "Sintomas são obrigatórios")
    @JsonProperty("Sintomas")
    private List<String> sintomas;
}

/**
 * DTO para resposta do atendimento
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class AtendimentoResponseDTO {

    @JsonProperty("mensagem")
    private String mensagem;

    @JsonProperty("possiveisDiagnosticos")
    private List<String> possiveisDiagnosticos;

    @JsonProperty("tratamentoSugerido")
    private String tratamentoSugerido;

    @JsonProperty("exameSolicitado")
    private ExameSolicitadoDTO exameSolicitado;
}

/**
 * DTO para exame solicitado
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class ExameSolicitadoDTO {

    @JsonProperty("tipoExame")
    private String tipoExame;

    @JsonProperty("mensagem")
    private String mensagem;

    @JsonProperty("codigoExame")
    private String codigoExame;
}

/**
 * DTO para verificação de disponibilidade
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class DisponibilidadeDTO {
    
    private String horario;
    private String especialidade;
}

/**
 * DTO para criação de consulta via mensageria
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class CriarConsultaDTO {
    
    private Long consultaId;
    private String cpfPaciente;
    private String nomePaciente;
    private String horario;
    private String especialidadeMedico;
}
