package com.hospital.agendamento.repository;

import com.hospital.agendamento.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository para operações com Pacientes
 */
@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    
    /**
     * Busca paciente por CPF
     */
    Optional<Paciente> findByCpf(String cpf);
    
    /**
     * Verifica se existe paciente com determinado CPF
     */
    boolean existsByCpf(String cpf);
}
