package com.hospital.agendamento.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para requisição de cadastro de exame
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExameRequestDTO {

    @Valid
    @NotNull(message = "Dados do paciente são obrigatórios")
    @JsonProperty("Paciente")
    private PacienteDTO paciente;

    @NotBlank(message = "Horário é obrigatório")
    @JsonProperty("Horario")
    private String horario; // Formato: "dia e hora"

    @NotBlank(message = "Tipo de exame é obrigatório")
    @JsonProperty("Exame")
    private String exame; // Ex: "Tipo do exame. Ex: coleta de sangue"

    /**
     * DTO interno para dados do paciente
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PacienteDTO {
        
        @NotBlank(message = "Nome é obrigatório")
        @JsonProperty("Nome")
        private String nome;

        @NotBlank(message = "CPF é obrigatório")
        @JsonProperty("CPF")
        private String cpf;

        @NotNull(message = "Idade é obrigatória")
        @JsonProperty("idade")
        private Integer idade;

        @NotBlank(message = "Sexo é obrigatório")
        @JsonProperty("Sexo")
        private String sexo;
    }
}
