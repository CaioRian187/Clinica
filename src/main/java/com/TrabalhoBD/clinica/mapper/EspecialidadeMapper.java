package com.TrabalhoBD.clinica.mapper;

import com.TrabalhoBD.clinica.dtos.EspecialidadeResponseDTO;
import com.TrabalhoBD.clinica.models.Especialidade;

public class EspecialidadeMapper {

    public static EspecialidadeResponseDTO toDtoFromEntity(Especialidade especialidade){
        return new EspecialidadeResponseDTO(
                especialidade.getId(),
                especialidade.getNome()
        );
    }

    public static Especialidade toEntityFromDto(EspecialidadeResponseDTO dto){
        return new Especialidade(
                dto.id(),
                dto.nome()
        );
    }
}
