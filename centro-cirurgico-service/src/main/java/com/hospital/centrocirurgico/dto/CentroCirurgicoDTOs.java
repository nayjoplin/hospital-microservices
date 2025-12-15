package com.hospital.centrocirurgico.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para marcar procedimento
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarcarProcedimentoDTO {

    @NotBlank(message = "CPF do paciente é obrigatório")
    @JsonProperty("CPF paciente")
    private String cpfPaciente;

    @NotBlank(message = "Código do exame é obrigatório")
    @JsonProperty("Codigo exame")
    private String codigoExame;

    @NotBlank(message = "Horário é obrigatório")
    @JsonProperty("Horario desejado")
    private String horarioDesejado;
}

/**
 * DTO para criar exame via mensageria
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class CriarExameDTO {
    private Long exameId;
    private String cpfPaciente;
    private String nomePaciente;
    private String horario;
    private String tipoExame;
}

/**
 * DTO para criar solicitação de exame
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class CriarSolicitacaoDTO {
    private String cpfPaciente;
    private String nomePaciente;
    private String tipoExame;
    private String prioridade;
    private String solicitadoPor;
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
    private String tipoExame;
}

/**
 * DTO de resposta
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class ResponseDTO {
    
    @JsonProperty("mensagem")
    private String mensagem;
    
    @JsonProperty("codigo")
    private String codigo;
    
    @JsonProperty("codigoExame")
    private String codigoExame;
}
