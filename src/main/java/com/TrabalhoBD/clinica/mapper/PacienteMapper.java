package com.TrabalhoBD.clinica.mapper;

import com.TrabalhoBD.clinica.dtos.PacienteResponseDTO;
import com.TrabalhoBD.clinica.models.Paciente;

public abstract class PacienteMapper {

    public static PacienteResponseDTO toDtoFromEntity(Paciente paciente){
        return new PacienteResponseDTO(
                paciente.getId(),
                paciente.getNome(),
                paciente.getCpf(),
                paciente.getDataNascimento(),
                paciente.getTelefone(),
                paciente.getEndereco()
        );
    }

    public static Paciente toEntityFromDto(PacienteResponseDTO dto){
        return new Paciente(
                dto.id(),
                dto.nome(),
                dto.cpf(),
                dto.dataNascimento(),
                dto.telefone(),
                dto.endereco()
        );
    }
}
