package com.TrabalhoBD.clinica.dtos;

import java.time.LocalDateTime;

public record ExameResponseDTO(
        Long id,
        String nome,
        LocalDateTime dataHora,
        String descricao,
        Long medicoId,
        String nomeMedico,
        Long pacienteId,
        String nomePaciente
) {
}
