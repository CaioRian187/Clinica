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


}
