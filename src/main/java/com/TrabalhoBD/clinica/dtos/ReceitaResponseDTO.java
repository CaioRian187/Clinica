package com.TrabalhoBD.clinica.dtos;


import java.time.LocalDateTime;

public record ReceitaResponseDTO(

        Long receitaId,
        LocalDateTime dataEmissao,
        String medicamento,
        String dosagem,
        String instrucoes,
        Long consultaId

) {
}
