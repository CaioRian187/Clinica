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

    public Paciente(Long id, String nome, String cpf, LocalDate dataNascimento, String telefone, String endereco) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.endereco = endereco;
    }

    private Paciente(PacienteBuilder builder) {
        this.nome = builder.nome;
        this.cpf = builder.cpf;
        this.dataNascimento = builder.dataNascimento;
        this.telefone = builder.telefone;
        this.endereco = builder.endereco;
        this.consultas = builder.consultas;
        this.exames = builder.exames;
    }

    public static class PacienteBuilder {
        private String nome;
        private String cpf;
        private LocalDate dataNascimento;
        private String telefone;
        private String endereco;
        private List<Consulta> consultas = new ArrayList<>();
        private List<Exame> exames = new ArrayList<>();

        public PacienteBuilder() {
        }

        public PacienteBuilder adicionarNome(String nome){
            this.nome = nome;
            return this;
        }

        public PacienteBuilder adicionarCpf(String cpf){
            this.cpf = cpf;
            return this;
        }

        public PacienteBuilder adicionarDataNascimento(LocalDate dataNascimente){
            this.dataNascimento = dataNascimente;
            return this;
        }

        public PacienteBuilder adicionarTelefone(String telefone) {
            this.telefone = telefone;
            return this;
        }

        public PacienteBuilder adicionarEndereco(String endereco) {
            this.endereco = endereco;
            return this;
        }

        public Paciente build() {
            if (this.nome == null || this.nome.trim().isEmpty()) {
                throw new IllegalStateException("O nome do paciente é obrigatório.");
            }
            if (this.cpf == null || this.cpf.trim().isEmpty()) {
                throw new IllegalStateException("O CPF do paciente é obrigatório.");
            }
            if (this.dataNascimento == null) {
                throw new IllegalStateException("A data de nascimento do paciente é obrigatória.");
            }
            return new Paciente(this);
        }
    }
}
