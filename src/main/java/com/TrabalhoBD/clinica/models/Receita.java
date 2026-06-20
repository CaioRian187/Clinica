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
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "receita")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Receita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_receita", unique = true)
    private Long id;

    @Column(name ="data_emissão", nullable = false)
    private LocalDateTime dataEmissao;

    @Column(name = "medicamento", nullable = false, length = 255)
    @NotBlank
    private String medicamento;

    @Column(name = "dosagem", nullable = false, length = 255)
    @NotBlank
    private String dosagem;

    @Column(name = "instrucoes", nullable = false, length = 255)
    @NotBlank
    private String instrucoes;

    @ManyToOne
    @JoinColumn(name = "consulta_id", nullable = false)
    private Consulta consulta;

    private Receita(ReceitaBuilder builder) {
        this.dataEmissao = builder.dataEmissao;
        this.medicamento = builder.medicamento;
        this.dosagem = builder.dosagem;
        this.instrucoes = builder.instrucoes;
        this.consulta = builder.consulta;
    }

    public static class ReceitaBuilder {
        private LocalDateTime dataEmissao;
        private String medicamento;
        private String dosagem;
        private String instrucoes;
        private Consulta consulta;

        public ReceitaBuilder() {
        }

        public ReceitaBuilder adicionarDataEmissao(LocalDateTime dataEmissao){
            this.dataEmissao = dataEmissao;
            return this;
        }

        public ReceitaBuilder adicionarMedicamento(String medicamento){
            this.medicamento = medicamento;
            return this;
        }

        public ReceitaBuilder adicionarDosagem(String dosagem) {
            this.dosagem = dosagem;
            return this;
        }

        public ReceitaBuilder adicionarInstrucoes(String instrucoes) {
            this.instrucoes = instrucoes;
            return this;
        }

        public ReceitaBuilder adicionarConsulta(Consulta consulta){
            this.consulta = consulta;
            return this;
        }

        public Receita build() {
            if (this.dataEmissao == null) {
                throw new IllegalStateException("A data de emissão da receita é obrigatória.");
            }
            if (this.medicamento == null || this.medicamento.isBlank()) {
                throw new IllegalStateException("O nome do medicamento é obrigatório.");
            }
            if (this.consulta == null) {
                throw new IllegalStateException("A receita deve estar vinculada a uma consulta válida.");
            }
            if (this.dosagem == null || this.dosagem.isBlank()) {
                throw new IllegalStateException("A dosagem do medicamento é obrigatória.");
            }
            if (this.instrucoes == null || this.instrucoes.isBlank()) {
                throw new IllegalStateException("As instruções de uso são obrigatórias.");
            }

            return new Receita(this);
        }
    }
}
