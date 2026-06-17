package com.TrabalhoBD.clinica.dtos;

import java.util.Set;

public record MedicoResponseDTO(
        Long id,
        String nome,
        String crm,
        String telefone,
        Set<EspecialidadeResponseDTO> especialidades
) {
}
