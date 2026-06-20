package com.TrabalhoBD.clinica.mapper;

import com.TrabalhoBD.clinica.dtos.EspecialidadeResponseDTO;
import com.TrabalhoBD.clinica.dtos.MedicoResponseDTO;
import com.TrabalhoBD.clinica.models.Medico;

import java.util.stream.Collectors;

public abstract class MedicoMapper {

    public static MedicoResponseDTO toDtoFromEntity(Medico medico){
        return new MedicoResponseDTO(
                medico.getId(),
                medico.getNome(),
                medico.getCrm(),
                medico.getTelefone(),
                medico.getEspecialidades().stream().map(
                        entity -> new EspecialidadeResponseDTO(
                                entity.getId(),
                                entity.getNome())
                ).collect(Collectors.toSet()));
    }

    public static Medico toEntityFromDto(MedicoResponseDTO medico){
        return new Medico(
                medico.id(),
                medico.nome(),
                medico.crm(),
                medico.telefone(),
                medico.especialidades()
        );
    }
}
