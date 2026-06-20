package com.TrabalhoBD.clinica.dtos;

import java.time.LocalDateTime;
import java.util.List;

public record ConsultaResponseDTO(

        Long id,
        LocalDateTime datahora,
        String observacoes,
        Long medicoId,
        String nomeMedico,
        Long pacienteId,
        String nomePaciente,
        List<ReceitaResponseDTO> receitas

) {
}
