package com.TrabalhoBD.clinica.mapper;

import com.TrabalhoBD.clinica.dtos.ReceitaResponseDTO;
import com.TrabalhoBD.clinica.models.Consulta;
import com.TrabalhoBD.clinica.models.Receita;

public abstract class ReceitaMapper {

    public static ReceitaResponseDTO toDtoFromEntity(Receita receita) {
        return new ReceitaResponseDTO(
                receita.getId(),
                receita.getDataEmissao(),
                receita.getMedicamento(),
                receita.getDosagem(),
                receita.getInstrucoes(),
                receita.getConsulta().getId(),
                receita.getConsulta().getPaciente().getNome());
    }

    public static Receita toEntityFromDto(ReceitaResponseDTO dto, Consulta consulta) {
        return new Receita(
                dto.receitaId(),
                dto.dataEmissao(),
                dto.medicamento(),
                dto.dosagem(),
                dto.instrucoes(),
                consulta);
    }

}
