package com.TrabalhoBD.clinica.dtos;

import java.time.LocalDateTime;

public record ExameResponseDTO(
        Long id,
        String nome,
        String data,
        LocalDateTime dataHora,
        Long medicoId,
        Long pacienteId
) {
}
