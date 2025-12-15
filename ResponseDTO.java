package com.hospital.agendamento.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para resposta padrão de sucesso
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO {

    @JsonProperty("mensagem")
    private String mensagem;

    @JsonProperty("codigo")
    private String codigo;
}

/**
 * DTO para resposta de erro/conflito
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class ErrorResponseDTO {

    @JsonProperty("mensagem")
    private String mensagem;

    @JsonProperty("detalhes")
    private String detalhes;
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
    private String tipoServico; // consulta ou exame
    private String especialidadeOuTipo;
    private boolean disponivel;
}
