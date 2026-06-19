package com.TrabalhoBD.clinica.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "exame")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Exame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_exame", unique = true)
    private Long id;

    @Column(name = "nome", length = 255, nullable = false)
    @NotBlank
    private String nome;

    @Column(name = "dataHora", nullable = false)
    @NotNull
    private LocalDateTime dataHora;

    @Column(name = "descrição", length = 255, nullable = false)
    @NotBlank
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "medico_id", nullable = false, updatable = false)
    private Medico medico;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false, updatable = false)
    private Paciente paciente;

    private Exame(ExameBuilder builder) {
        this.nome = builder.nome;
        this.dataHora = builder.dataHora;
        this.descricao = builder.descricao;
        this.medico = builder.medico;
        this.paciente = builder.paciente;
    }

    public static class ExameBuilder {
        private String nome;
        private LocalDateTime dataHora;
        private String descricao;
        private Medico medico;
        private Paciente paciente;

        public ExameBuilder() {

        }

        public ExameBuilder adicionarNome(String nome){
            this.nome = nome;
            return this;
        }

        public ExameBuilder adicionarDataHora(LocalDateTime dataHora){
            this.dataHora = dataHora;
            return this;
        }

        public ExameBuilder adicionarDescricao(String descricao) {
            this.descricao = descricao;
            return this;
        }

        public ExameBuilder adicionarMedico(Medico medico){
            this.medico = medico;
            return this;
        }

        public ExameBuilder adicionarPaciente(Paciente paciente){
            this.paciente = paciente;
            return this;
        }

        public Exame build() {
            if (this.nome == null || this.nome.isBlank()) {
                throw new IllegalStateException("O nome do exame é obrigatório.");
            }
            if (this.dataHora == null) {
                throw new IllegalStateException("A data e hora do exame são obrigatórias.");
            }
            if (this.medico == null) {
                throw new IllegalStateException("Um médico válido deve ser associado ao exame.");
            }
            if (this.paciente == null) {
                throw new IllegalStateException("Um paciente válido deve ser associado ao exame.");
            }

            if (this.descricao == null || this.descricao.isBlank()) {
                this.descricao = "Nenhuma descrição informada.";
            }

            return new Exame(this);
        }
    }

}
