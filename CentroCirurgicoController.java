package com.hospital.centrocirurgico.controller;

import com.hospital.centrocirurgico.dto.*;
import com.hospital.centrocirurgico.entity.Procedimento;
import com.hospital.centrocirurgico.service.CentroCirurgicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller do Centro Cirúrgico
 */
@RestController
@RequestMapping("/api/procedimentos")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Centro Cirúrgico", description = "Endpoints do centro cirúrgico")
@SecurityRequirement(name = "bearer-jwt")
public class CentroCirurgicoController {

    private final CentroCirurgicoService centroCirurgicoService;

    /**
     * Endpoint para marcar procedimento
     */
    @PostMapping("/marcar")
    @Operation(summary = "Marcar procedimento com horário")
    @PreAuthorize("hasAnyRole('USUARIO', 'MEDICO', 'ADMIN')")
    public ResponseEntity<ResponseDTO> marcarProcedimento(
            @Valid @RequestBody MarcarProcedimentoDTO dto) {
        
        log.info("Recebida requisição para marcar procedimento");
        ResponseDTO response = centroCirurgicoService.marcarProcedimento(dto);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para criar solicitação de exame
     */
    @PostMapping("/criar-solicitacao")
    @Operation(summary = "Criar solicitação de exame")
    @PreAuthorize("hasAnyRole('MEDICO', 'ADMIN')")
    public ResponseEntity<ResponseDTO> criarSolicitacao(
            @Valid @RequestBody CriarSolicitacaoDTO dto) {
        
        log.info("Recebida solicitação de exame");
        ResponseDTO response = centroCirurgicoService.criarSolicitacao(dto);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para verificar disponibilidade
     */
    @PostMapping("/verificar-disponibilidade")
    @Operation(summary = "Verificar disponibilidade de horário")
    public ResponseEntity<Boolean> verificarDisponibilidade(
            @RequestBody DisponibilidadeDTO dto) {
        
        log.info("Verificando disponibilidade");
        boolean disponivel = centroCirurgicoService.verificarDisponibilidade(
            dto.getHorario(), dto.getTipoExame());
        return ResponseEntity.ok(disponivel);
    }

    /**
     * Endpoint para buscar procedimentos por CPF
     */
    @GetMapping("/cpf/{cpf}")
    @Operation(summary = "Buscar procedimentos por CPF")
    @PreAuthorize("hasAnyRole('USUARIO', 'MEDICO', 'ADMIN')")
    public ResponseEntity<List<Procedimento>> buscarPorCpf(@PathVariable String cpf) {
        log.info("Buscando procedimentos para CPF: {}", cpf);
        List<Procedimento> procedimentos = centroCirurgicoService.buscarPorCpf(cpf);
        return ResponseEntity.ok(procedimentos);
    }
}
