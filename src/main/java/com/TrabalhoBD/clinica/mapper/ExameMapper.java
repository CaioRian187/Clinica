package com.TrabalhoBD.clinica.mapper;

import com.TrabalhoBD.clinica.dtos.ExameResponseDTO;
import com.TrabalhoBD.clinica.models.Exame;

public abstract class ExameMapper {

    public static ExameResponseDTO toDtoFromEntity(Exame exame){
        return new ExameResponseDTO(
                exame.getId(),
                exame.getNome(),
                exame.getDataHora(),
                exame.getDescricao(),
                exame.getMedico().getId(),
                exame.getMedico().getNome(),
                exame.getPaciente().getId(),
                exame.getPaciente().getNome()
        );
    }
}
