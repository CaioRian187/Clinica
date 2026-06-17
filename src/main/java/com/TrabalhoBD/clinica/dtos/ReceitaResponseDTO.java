package com.TrabalhoBD.clinica.dtos;

import java.time.LocalDate;

public record ReceitaResponseDTO(

        Long receitaId,
        LocalDate dataEmissao,
        String medicamento,
        String dosagem,
        String instrucoes,
        Long consultaId

) {
}
