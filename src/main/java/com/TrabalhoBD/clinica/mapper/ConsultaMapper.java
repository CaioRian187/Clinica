package com.TrabalhoBD.clinica.mapper;

import com.TrabalhoBD.clinica.dtos.ConsultaResponseDTO;
import com.TrabalhoBD.clinica.dtos.ReceitaResponseDTO;
import com.TrabalhoBD.clinica.models.Consulta;

public abstract class ConsultaMapper {

    public static ConsultaResponseDTO toDtoFromEntity(Consulta consulta){
        return new ConsultaResponseDTO(
                consulta.getId(),
                consulta.getDataHora(),
                consulta.getObservacoes(),
                consulta.getMedico().getId(),
                consulta.getMedico().getNome(),
                consulta.getPaciente().getId(),
                consulta.getPaciente().getNome(),
                consulta.getReceitas().stream().map(
                        entity -> new ReceitaResponseDTO(
                                entity.getId(),
                                entity.getDataEmissao(),
                                entity.getMedicamento(),
                                entity.getDosagem(),
                                entity.getInstrucoes(),
                                entity.getConsulta().getId()
                        )).toList()
        );
    }
}
