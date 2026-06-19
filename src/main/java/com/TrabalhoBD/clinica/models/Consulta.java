package com.TrabalhoBD.clinica.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "consulta")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode

public class Consulta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_consulta", unique = true)
    private Long id;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Column(name = "observacoes", nullable = false, columnDefinition = "TEXT")
    @NotBlank
    private String observacoes;

    @ManyToOne
    @JoinColumn(name = "id_medico", nullable =  false)
    private Medico medico;

    @ManyToOne
    @JoinColumn(name = "id_paciente", nullable = false)
    private Paciente paciente;

    @OneToMany(mappedBy = "consulta", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty(access = Access.WRITE_ONLY)
    private List<Receita> receitas = new ArrayList<>();

    private Consulta(ConsultaBuilder builder) {
        this.dataHora = builder.dataHora;
        this.observacoes = builder.observacoes;
        this.medico = builder.medico;
        this.paciente = builder.paciente;
        this.receitas = builder.receitas;
    }

    public static class ConsultaBuilder {
        private LocalDateTime dataHora;
        private String observacoes;
        private Medico medico;
        private Paciente paciente;
        private List<Receita> receitas = new ArrayList<>();

        public ConsultaBuilder() {

        }

        public ConsultaBuilder adicionarDataHora(LocalDateTime dataHora){
            this.dataHora = dataHora;
            return this;
        }

        public ConsultaBuilder adicionarObservacoes(String observacoes) {
            this.observacoes = observacoes;
            return this;
        }

        public ConsultaBuilder adicionarMedico(Medico medico){
            this.medico = medico;
            return this;
        }

        public ConsultaBuilder adicionarPaciente(Paciente paciente){
            this.paciente = paciente;
            return this;
        }


        public ConsultaBuilder adicionarReceita(Receita receita) {
            this.receitas.add(receita);
            return this;
        }

        public Consulta build() {
            if (this.dataHora == null) {
                throw new IllegalStateException("A data e hora da consulta não podem ser nulas.");
            }
            if (this.medico == null) {
                throw new IllegalStateException("Um médico válido deve ser associado à consulta.");
            }
            if (this.paciente == null) {
                throw new IllegalStateException("Um paciente válido deve ser associado à consulta.");
            }
            if (this.observacoes == null || this.observacoes.isBlank()) {
                this.observacoes = "Sem observações registradas.";
            }
            return new Consulta(this);
        }
    }
}
