package com.TrabalhoBD.clinica.dtos;

import jakarta.validation.constraints.NotBlank;

public record EspecialidadeRequestDTO(
        @NotBlank(message = "O nome da especialidade é obrigatório.")
        String nome
) {
}
