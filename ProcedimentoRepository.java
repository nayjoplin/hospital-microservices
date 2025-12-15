package com.hospital.centrocirurgico.repository;

import com.hospital.centrocirurgico.entity.Procedimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository para operações com Procedimentos
 */
@Repository
public interface ProcedimentoRepository extends JpaRepository<Procedimento, Long> {
    
    List<Procedimento> findByCpfPaciente(String cpfPaciente);
    
    Optional<Procedimento> findByIdAgendamento(Long idAgendamento);
    
    @Query("SELECT p FROM Procedimento p WHERE p.tipoProcedimento = :tipo " +
           "AND p.horario = :horario AND p.status != 'CANCELADO'")
    Optional<Procedimento> findByTipoAndHorario(
        @Param("tipo") String tipo,
        @Param("horario") LocalDateTime horario
    );
    
    @Query("SELECT p FROM Procedimento p WHERE p.prioridade = :prioridade " +
           "AND p.status = 'AGUARDANDO_AGENDAMENTO'")
    List<Procedimento> findProcedimentosAguardandoPorPrioridade(
        @Param("prioridade") Procedimento.PrioridadeProcedimento prioridade
    );
    
    List<Procedimento> findByTipoProcedimentoContainingIgnoreCase(String tipo);
}
