package com.hospital.agendamento.repository;

import com.hospital.agendamento.entity.Exame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository para operações com Exames
 */
@Repository
public interface ExameRepository extends JpaRepository<Exame, Long> {
    
    /**
     * Busca exames por CPF do paciente
     */
    List<Exame> findByCpfPaciente(String cpfPaciente);
    
    /**
     * Verifica se existe exame para o paciente no horário especificado
     */
    @Query("SELECT COUNT(e) > 0 FROM Exame e WHERE e.cpfPaciente = :cpf " +
           "AND e.horario = :horario AND e.status != 'CANCELADO'")
    boolean existsExameByPacienteAndHorario(
        @Param("cpf") String cpf, 
        @Param("horario") LocalDateTime horario
    );
    
    /**
     * Busca exame por ID do centro cirúrgico
     */
    Optional<Exame> findByIdCentroCirurgico(Long idCentroCirurgico);
    
    /**
     * Busca exames por nome do paciente
     */
    List<Exame> findByNomePacienteContainingIgnoreCase(String nomePaciente);
    
    /**
     * Busca exames por tipo
     */
    List<Exame> findByTipoExameContainingIgnoreCase(String tipoExame);
}
