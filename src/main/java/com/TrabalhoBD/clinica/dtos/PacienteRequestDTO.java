package com.TrabalhoBD.clinica.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record PacienteRequestDTO(
        @NotBlank(message = "O nome do paciente é obrigatório.")
        String nome,

        @NotBlank(message = "O CPF é obrigatório.")
        String cpf,

        @NotNull(message = "A data de nascimento é obrigatória.")
        @Past(message = "A data de nascimento deve ser uma data no passado.")
        LocalDate dataNascimento,

        @NotBlank(message = "O telefone é obrigatório.")
        String telefone,

        @NotBlank(message = "O endereço é obrigatório.")
        String endereco
) {
}
