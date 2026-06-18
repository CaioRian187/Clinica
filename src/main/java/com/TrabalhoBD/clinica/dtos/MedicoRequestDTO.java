package com.TrabalhoBD.clinica.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record MedicoRequestDTO(

        @NotBlank(message = "O nome do médico é obrigatório.")
        String nome,

        @NotBlank(message = "O CRM é obrigatório.")
        String crm,

        @NotBlank(message = "O telefone é obrigatório.")
        String telefone

) {
}
