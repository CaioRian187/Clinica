package com.TrabalhoBD.clinica.models;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "especialidade")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Especialidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_especialidade", unique = true, nullable = false)
    private Long id;

    @Column(name = "nome_especialidade", nullable = false, length = 100)
    @NotBlank
    private String nome;

    @ManyToMany(mappedBy = "especialidades")
    @JsonProperty(access = Access.WRITE_ONLY)
    private Set<Medico> medicos = new HashSet<>();

    public Especialidade(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }
}