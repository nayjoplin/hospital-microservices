package com.hospital.agendamento.repository;

import com.hospital.agendamento.entity.Consulta;
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
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    
    /**
     * Busca consultas por CPF do paciente
     */
    List<Consulta> findByCpfPaciente(String cpfPaciente);
    
    /**
     * Verifica se existe consulta para o paciente no horário especificado
     */
    @Query("SELECT COUNT(c) > 0 FROM Consulta c WHERE c.cpfPaciente = :cpf " +
           "AND c.horario = :horario AND c.status != 'CANCELADA'")
    boolean existsConsultaByPacienteAndHorario(
        @Param("cpf") String cpf, 
        @Param("horario") LocalDateTime horario
    );
    
    /**
     * Busca consulta por ID da clínica
     */
    Optional<Consulta> findByIdClinica(Long idClinica);
    
    /**
     * Busca consultas por nome do paciente
     */
    List<Consulta> findByNomePacienteContainingIgnoreCase(String nomePaciente);
}
