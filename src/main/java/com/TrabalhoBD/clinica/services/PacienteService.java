package com.TrabalhoBD.clinica.services;

import java.util.List;
import java.util.Optional;

import com.TrabalhoBD.clinica.dtos.PacienteRequestDTO;
import com.TrabalhoBD.clinica.dtos.PacienteResponseDTO;
import com.TrabalhoBD.clinica.mapper.PacienteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.TrabalhoBD.clinica.exceptions.NotFoundException;
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

    public Paciente findByNome(String nome){
        Optional<Paciente> paciente = this.pacienteRepository.findByNome(nome);
        return paciente.orElseThrow( () -> new NotFoundException("Paciete de nome = " + nome + " não encontrado"));
    }

    public List<Paciente> findAll(){
        List<Paciente> list = this.pacienteRepository.findAll();
        if (list.isEmpty()){
            throw new NotFoundException("Nenhum paciente encontrado");
        }
        return list;
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
    public Paciente updatePaciente(Paciente paciente){
        Paciente newPaciente = this.pacienteRepository.findById(paciente.getId())
                .orElseThrow( () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Paciente de Id: " + paciente.getId() + " não encontrado"
                ));

        newPaciente.setNome(paciente.getNome());
        newPaciente.setCpf(paciente.getCpf());
        newPaciente.setDataNascimento(paciente.getDataNascimento());
        newPaciente.setTelefone(paciente.getTelefone());
        newPaciente.setEndereco(paciente.getEndereco());

        return this.pacienteRepository.save(newPaciente);
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
