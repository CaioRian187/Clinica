package com.TrabalhoBD.clinica.dtos;

import java.util.List;

public record AdicionarEspecialidadeRequestDTO(
        Long medicoId,
        List<Long> listEspecialidadesIds
) {
}
