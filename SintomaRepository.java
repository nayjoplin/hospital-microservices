package com.hospital.clinica.repository;

import com.hospital.clinica.entity.Sintoma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para operações com Sintomas
 */
@Repository
public interface SintomaRepository extends JpaRepository<Sintoma, Long> {
    
    Optional<Sintoma> findByNomeIgnoreCase(String nome);
    
    List<Sintoma> findByPrioridade(Sintoma.Prioridade prioridade);
    
    @Query("SELECT s FROM Sintoma s WHERE LOWER(s.nome) IN :nomes")
    List<Sintoma> findByNomesIn(@Param("nomes") List<String> nomes);
}
