package com.TrabalhoBD.clinica.dtos;

import com.TrabalhoBD.clinica.models.Receita;

import java.time.LocalDateTime;
import java.util.List;

public record ConsultaResponseDTO(

        Long id,
        LocalDateTime datahora,
        String observacoes,
        Long medicoId,
        Long pacienteId,
        List<ReceitaResponseDTO> receitas

) {
}
