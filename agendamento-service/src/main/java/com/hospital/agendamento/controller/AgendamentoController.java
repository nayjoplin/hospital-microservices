package com.hospital.agendamento.controller;

import com.hospital.agendamento.dto.ConsultaRequestDTO;
import com.hospital.agendamento.dto.ExameRequestDTO;
import com.hospital.agendamento.dto.ResponseDTO;
import com.hospital.agendamento.entity.Consulta;
import com.hospital.agendamento.entity.Exame;
import com.hospital.agendamento.service.ConsultaService;
import com.hospital.agendamento.service.ExameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para endpoints de agendamento de consultas e exames
 */
@RestController
@RequestMapping("/api/cadastro")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Agendamento", description = "Endpoints para agendamento de consultas e exames")
@SecurityRequirement(name = "bearer-jwt")
public class AgendamentoController {

    private final ConsultaService consultaService;
    private final ExameService exameService;

    @PostMapping("/consulta")
    @Operation(summary = "Cadastrar nova consulta", description = "Permite usuários agendarem consultas médicas")
    @PreAuthorize("hasAnyRole('USUARIO', 'MEDICO', 'ADMIN')")
    public ResponseEntity<ResponseDTO> cadastrarConsulta(@Valid @RequestBody ConsultaRequestDTO request) {
        log.info("Recebida requisição para cadastrar consulta");
        ResponseDTO response = consultaService.cadastrarConsulta(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/exame")
    @Operation(summary = "Cadastrar novo exame", description = "Permite usuários agendarem exames")
    @PreAuthorize("hasAnyRole('USUARIO', 'MEDICO', 'ADMIN')")
    public ResponseEntity<ResponseDTO> cadastrarExame(@Valid @RequestBody ExameRequestDTO request) {
        log.info("Recebida requisição para cadastrar exame");
        ResponseDTO response = exameService.cadastrarExame(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
