package com.TrabalhoBD.clinica.services;

import java.util.List;

import com.TrabalhoBD.clinica.dtos.PacienteRequestDTO;
import com.TrabalhoBD.clinica.dtos.PacienteResponseDTO;
import com.TrabalhoBD.clinica.mapper.PacienteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.TrabalhoBD.clinica.models.Paciente;
import com.TrabalhoBD.clinica.repositories.PacienteRepository;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;
    
    public PacienteResponseDTO findById(Long id){
        Paciente paciente = this.pacienteRepository.findById(id)
                .orElseThrow( () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Paciente de Id: " + id + " não encontrado"
                ));

        return PacienteMapper.toDtoFromEntity(paciente);
    }

    public PacienteResponseDTO findByNome(String nome){

        Paciente paciente = this.pacienteRepository.findByNome(nome)
                .orElseThrow( () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Paciente de nome: " + nome + " não encontrado."
                ));
        return PacienteMapper.toDtoFromEntity(paciente);
    }

    public List<PacienteResponseDTO> findAll(){
        return this.pacienteRepository.findAll()
                .stream().map(PacienteMapper::toDtoFromEntity)
                .toList();
    }

    @Transactional
    public PacienteResponseDTO createPaciente(PacienteRequestDTO dto){

        Paciente paciente = new Paciente.PacienteBuilder()
                .adicionarNome(dto.nome())
                .adicionarCpf(dto.cpf())
                .adicionarDataNascimento(dto.dataNascimento())
                .adicionarTelefone(dto.telefone())
                .adicionarEndereco(dto.endereco())
                .build();

        this.pacienteRepository.save(paciente);

        return PacienteMapper.toDtoFromEntity(paciente);
    }

    @Transactional
    public PacienteResponseDTO updatePaciente(Long pacienteId, PacienteRequestDTO dto){
        Paciente paciente = this.pacienteRepository.findById(pacienteId)
                .orElseThrow( () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Paciente de Id: " + pacienteId + " não encontrado"
                ));

        paciente.setNome(dto.nome());
        paciente.setCpf(dto.cpf());
        paciente.setDataNascimento(dto.dataNascimento());
        paciente.setTelefone(dto.telefone());
        paciente.setEndereco(dto.endereco());

        this.pacienteRepository.save(paciente);

        return PacienteMapper.toDtoFromEntity(paciente);
    }

    public void deletePaciente(Long id){
        findById(id);
        try{
            this.pacienteRepository.deleteById(id);
        }catch (DataIntegrityViolationException exception){
            throw new DataIntegrityViolationException("Não é possível excluir, pois o paciente possui vinculações");
        }
    }
}
