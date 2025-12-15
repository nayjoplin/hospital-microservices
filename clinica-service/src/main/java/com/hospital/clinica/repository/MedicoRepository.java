package com.hospital.clinica.repository;

import com.hospital.clinica.entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para operações com Médicos
 */
@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {
    
    Optional<Medico> findByCrm(String crm);
    
    List<Medico> findByEspecialidade(String especialidade);
    
    boolean existsByCrm(String crm);
}
