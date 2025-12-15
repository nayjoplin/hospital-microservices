package com.hospital.clinica.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Entidade que representa um Sintoma
 */
@Entity
@Table(name = "sintomas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sintoma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome do sintoma é obrigatório")
    @Column(nullable = false, unique = true, length = 100)
    private String nome;

    @NotNull(message = "Prioridade é obrigatória")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Prioridade prioridade;

    @ManyToMany(mappedBy = "sintomas")
    private Set<Doenca> doencas = new HashSet<>();

    /**
     * Enum para prioridade do sintoma
     * 1- baixa, 2- padrão, 3- alta, 4- Emergencial
     */
    public enum Prioridade {
        BAIXA(1),
        PADRAO(2),
        ALTA(3),
        EMERGENCIAL(4);

        private final int nivel;

        Prioridade(int nivel) {
            this.nivel = nivel;
        }

        public int getNivel() {
            return nivel;
        }
    }
}

/**
 * Entidade que representa uma Doença
 */
@Entity
@Table(name = "doencas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class Doenca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome da doença é obrigatório")
    @Column(nullable = false, unique = true, length = 100)
    private String nome;

    @ManyToMany
    @JoinTable(
        name = "doenca_sintoma",
        joinColumns = @JoinColumn(name = "doenca_id"),
        inverseJoinColumns = @JoinColumn(name = "sintoma_id")
    )
    private Set<Sintoma> sintomas = new HashSet<>();

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "tratamento_padrao", columnDefinition = "TEXT")
    private String tratamentoPadrao;

    @Column(name = "requer_exame_alta_complexidade")
    private Boolean requerExameAltaComplexidade = false;

    @Column(name = "tipo_exame_sugerido", length = 100)
    private String tipoExameSugerido;
}
