package com.TrabalhoBD.clinica.dtos;

import java.time.LocalDate;

public record PacienteResponseDTO(
        Long id,
        String nome,
        String cpf,
        LocalDate dataNascimento,
        String telefone,
        String endereco
) {
}
