package com.hospital.clinica.repository;

import com.hospital.clinica.entity.ConsultaClinica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository para operações com Consultas
 */
@Repository
public interface ConsultaClinicaRepository extends JpaRepository<ConsultaClinica, Long> {
    
    List<ConsultaClinica> findByCpfPaciente(String cpfPaciente);
    
    Optional<ConsultaClinica> findByIdAgendamento(Long idAgendamento);
    
    @Query("SELECT c FROM ConsultaClinica c WHERE c.cpfPaciente = :cpf " +
           "AND c.horario = :horario")
    Optional<ConsultaClinica> findByCpfAndHorario(
        @Param("cpf") String cpf, 
        @Param("horario") LocalDateTime horario
    );
    
    @Query("SELECT c FROM ConsultaClinica c WHERE c.medico.especialidade = :especialidade " +
           "AND c.horario = :horario AND c.status != 'CANCELADA'")
    Optional<ConsultaClinica> findByEspecialidadeAndHorario(
        @Param("especialidade") String especialidade,
        @Param("horario") LocalDateTime horario
    );
}
