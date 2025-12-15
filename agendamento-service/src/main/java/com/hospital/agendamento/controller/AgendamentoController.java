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

    /**
     * Endpoint para cadastrar nova consulta
     */
    @PostMapping("/consulta")
    @Operation(summary = "Cadastrar nova consulta", 
               description = "Permite usuários agendarem consultas médicas")
    @PreAuthorize("hasAnyRole('USUARIO', 'MEDICO', 'ADMIN')")
    public ResponseEntity<ResponseDTO> cadastrarConsulta(
            @Valid @RequestBody ConsultaRequestDTO request) {
        log.info("Recebida requisição para cadastrar consulta");
        ResponseDTO response = consultaService.cadastrarConsulta(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Endpoint para cadastrar novo exame
     */
    @PostMapping("/exame")
    @Operation(summary = "Cadastrar novo exame", 
               description = "Permite usuários agendarem exames")
    @PreAuthorize("hasAnyRole('USUARIO', 'MEDICO', 'ADMIN')")
    public ResponseEntity<ResponseDTO> cadastrarExame(
            @Valid @RequestBody ExameRequestDTO request) {
        log.info("Recebida requisição para cadastrar exame");
        ResponseDTO response = exameService.cadastrarExame(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

/**
 * Controller para endpoints de pesquisa
 */
@RestController
@RequestMapping("/api/pesquisa")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Pesquisa", description = "Endpoints para pesquisa de consultas e exames")
@SecurityRequirement(name = "bearer-jwt")
class PesquisaController {

    private final ConsultaService consultaService;
    private final ExameService exameService;

    /**
     * Busca consultas por CPF
     */
    @GetMapping("/consultas/cpf/{cpf}")
    @Operation(summary = "Buscar consultas por CPF")
    @PreAuthorize("hasAnyRole('USUARIO', 'MEDICO', 'ADMIN')")
    public ResponseEntity<List<Consulta>> buscarConsultasPorCpf(@PathVariable String cpf) {
        log.info("Buscando consultas para CPF: {}", cpf);
        List<Consulta> consultas = consultaService.buscarConsultasPorCpf(cpf);
        return ResponseEntity.ok(consultas);
    }

    /**
     * Busca consultas por nome
     */
    @GetMapping("/consultas/nome/{nome}")
    @Operation(summary = "Buscar consultas por nome do paciente")
    @PreAuthorize("hasAnyRole('MEDICO', 'ADMIN')")
    public ResponseEntity<List<Consulta>> buscarConsultasPorNome(@PathVariable String nome) {
        log.info("Buscando consultas para nome: {}", nome);
        List<Consulta> consultas = consultaService.buscarConsultasPorNome(nome);
        return ResponseEntity.ok(consultas);
    }

    /**
     * Busca exames por CPF
     */
    @GetMapping("/exames/cpf/{cpf}")
    @Operation(summary = "Buscar exames por CPF")
    @PreAuthorize("hasAnyRole('USUARIO', 'MEDICO', 'ADMIN')")
    public ResponseEntity<List<Exame>> buscarExamesPorCpf(@PathVariable String cpf) {
        log.info("Buscando exames para CPF: {}", cpf);
        List<Exame> exames = exameService.buscarExamesPorCpf(cpf);
        return ResponseEntity.ok(exames);
    }

    /**
     * Busca exames por nome
     */
    @GetMapping("/exames/nome/{nome}")
    @Operation(summary = "Buscar exames por nome do paciente")
    @PreAuthorize("hasAnyRole('MEDICO', 'ADMIN')")
    public ResponseEntity<List<Exame>> buscarExamesPorNome(@PathVariable String nome) {
        log.info("Buscando exames para nome: {}", nome);
        List<Exame> exames = exameService.buscarExamesPorNome(nome);
        return ResponseEntity.ok(exames);
    }

    /**
     * Busca exames por tipo
     */
    @GetMapping("/exames/tipo/{tipo}")
    @Operation(summary = "Buscar exames por tipo")
    @PreAuthorize("hasAnyRole('MEDICO', 'ADMIN')")
    public ResponseEntity<List<Exame>> buscarExamesPorTipo(@PathVariable String tipo) {
        log.info("Buscando exames do tipo: {}", tipo);
        List<Exame> exames = exameService.buscarExamesPorTipo(tipo);
        return ResponseEntity.ok(exames);
    }
}

/**
 * Controller administrativo
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Administração", description = "Endpoints administrativos")
@SecurityRequirement(name = "bearer-jwt")
class AdminController {

    private final ConsultaService consultaService;
    private final ExameService exameService;

    /**
     * Cancela uma consulta
     */
    @DeleteMapping("/consultas/{id}")
    @Operation(summary = "Cancelar consulta")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> cancelarConsulta(@PathVariable Long id) {
        log.info("Cancelando consulta: {}", id);
        consultaService.cancelarConsulta(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Cancela um exame
     */
    @DeleteMapping("/exames/{id}")
    @Operation(summary = "Cancelar exame")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> cancelarExame(@PathVariable Long id) {
        log.info("Cancelando exame: {}", id);
        exameService.cancelarExame(id);
        return ResponseEntity.noContent().build();
    }
}
