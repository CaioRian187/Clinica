package com.TrabalhoBD.clinica.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "paciente")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paciente", unique = true)
    private Long id;

    @Column(name = "nome_paciente", nullable = false, length = 255)
    @NotBlank
    private String nome;

    @Column(name = "cpf", unique = true, nullable = false, length = 255)
    @NotBlank
    private String cpf;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "telefone", unique = true, nullable = false, length = 255)
    @NotBlank
    private String telefone;

    @Column(name = "endereço", nullable = false, length = 255)
    @NotBlank
    private String endereco;

    @OneToMany(mappedBy = "paciente")
    @JsonProperty(access = Access.WRITE_ONLY)
    private List<Consulta> consultas = new ArrayList<Consulta>();

    @OneToMany(mappedBy = "paciente")
    @JsonProperty(access = Access.WRITE_ONLY)
    private List<Exame> exames = new ArrayList<Exame>();

}
