package com.hospital.clinica.controller;

import com.hospital.clinica.dto.*;
import com.hospital.clinica.entity.Medico;
import com.hospital.clinica.service.ClinicaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller do Serviço de Clínica
 */
@RestController
@RequestMapping("/api/clinica")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Clínica", description = "Endpoints do serviço de clínica")
@SecurityRequirement(name = "bearer-jwt")
public class ClinicaController {

    private final ClinicaService clinicaService;

    /**
     * Endpoint para atender consulta
     */
    @PostMapping("/AtenderConsulta")
    @Operation(summary = "Atender consulta médica")
    @PreAuthorize("hasAnyRole('MEDICO', 'ADMIN')")
    public ResponseEntity<AtendimentoResponseDTO> atenderConsulta(
            @Valid @RequestBody Map<String, Object> request) {
        
        log.info("Recebida requisição para atender consulta");

        // Verificar se é por horário ou por código
        if (request.containsKey("Horario")) {
            AtenderConsultaHorarioDTO dto = AtenderConsultaHorarioDTO.builder()
                .cpfPaciente((String) request.get("CPF paciente"))
                .horario((String) request.get("Horario"))
                .sintomas((List<String>) request.get("Sintomas"))
                .build();
            
            AtendimentoResponseDTO response = clinicaService
                .atenderConsultaPorHorario(dto);
            return ResponseEntity.ok(response);
        } else {
            AtenderConsultaCodigoDTO dto = AtenderConsultaCodigoDTO.builder()
                .cpfPaciente((String) request.get("CPF paciente"))
                .codigoConsulta((String) request.get("Codigo consulta"))
                .sintomas((List<String>) request.get("Sintomas"))
                .build();
            
            AtendimentoResponseDTO response = clinicaService
                .atenderConsultaPorCodigo(dto);
            return ResponseEntity.ok(response);
        }
    }

    /**
     * Endpoint para verificar disponibilidade
     */
    @PostMapping("/verificar-disponibilidade")
    @Operation(summary = "Verificar disponibilidade de horário")
    public ResponseEntity<Boolean> verificarDisponibilidade(
            @RequestBody DisponibilidadeDTO dto) {
        
        log.info("Verificando disponibilidade: {}", dto);
        boolean disponivel = clinicaService.verificarDisponibilidade(
            dto.getHorario(), dto.getEspecialidade());
        return ResponseEntity.ok(disponivel);
    }
}
