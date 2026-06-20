package com.TrabalhoBD.clinica.services;

import java.util.List;

import com.TrabalhoBD.clinica.dtos.ExameRequestDTO;
import com.TrabalhoBD.clinica.dtos.ExameResponseDTO;
import com.TrabalhoBD.clinica.dtos.MedicoResponseDTO;
import com.TrabalhoBD.clinica.dtos.PacienteResponseDTO;
import com.TrabalhoBD.clinica.mapper.ExameMapper;
import com.TrabalhoBD.clinica.mapper.MedicoMapper;
import com.TrabalhoBD.clinica.mapper.PacienteMapper;
import com.TrabalhoBD.clinica.models.Medico;
import com.TrabalhoBD.clinica.models.Paciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.TrabalhoBD.clinica.exceptions.NotFoundException;
import com.TrabalhoBD.clinica.models.Exame;
import com.TrabalhoBD.clinica.repositories.ExameRepository;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ExameService {
    
    @Autowired
    private ExameRepository exameRepository;

    @Autowired
    private MedicoService medicoService;

    @Autowired
    private PacienteService pacienteService;

    public ExameResponseDTO findById(Long id){
        Exame exame = this.exameRepository.findById(id)
                .orElseThrow( () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Exame de Id: " + id + " não encontrado"
                ));
        return ExameMapper.toDtoFromEntity(exame);
    }

    public List<Exame> findByMedicoid(Long medicoId){
        List<Exame> exames = this.exameRepository.findByMedico_id(medicoId);

        if (exames.isEmpty()){
            throw new NotFoundException("Nenhum exame encontrado.");
        }
        return exames;
    }

    public List<Exame> findByPacienteId(Long pacienteId){
        List<Exame> exames = this.exameRepository.findByPaciente_id(pacienteId);

        if (exames.isEmpty()){
            throw new NotFoundException("Nenhum exame encontrado.");
        }
        return exames;
    }

    public List<Exame> findAll(){
        List<Exame> list = this.exameRepository.findAll();

        if (list.isEmpty()){
            throw new NotFoundException("Nenhum exame encontrado");
        }
        return list;
    }

    @Transactional
    public ExameResponseDTO create(ExameRequestDTO dto){

        MedicoResponseDTO medicoResponseDTO = this.medicoService.findById(dto.medicoId());
        Medico medico = MedicoMapper.toEntityFromDto(medicoResponseDTO);

        PacienteResponseDTO pacienteResponseDTO = this.pacienteService.findById(dto.pacienteId());
        Paciente paciente = PacienteMapper.toEntityFromDto(pacienteResponseDTO);

        Exame exame = new Exame.ExameBuilder()
                .adicionarNome(dto.nome())
                .adicionarDataHora(dto.dataHora())
                .adicionarDescricao(dto.descricao())
                .adicionarMedico(medico)
                .adicionarPaciente(paciente)
                .build();

        this.exameRepository.save(exame);

        return ExameMapper.toDtoFromEntity(exame);
    }


    @Transactional
    public ExameResponseDTO update(Long id, ExameRequestDTO dto){

        Exame exame = this.exameRepository.findById(id)
                .orElseThrow( () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Exame de Id: " + id + " não encontrado."
                ));

        if (!exame.getMedico().getId().equals(dto.medicoId())){
            MedicoResponseDTO medicoResponseDTO = this.medicoService.findById(dto.medicoId());
            Medico medico = MedicoMapper.toEntityFromDto(medicoResponseDTO);
            exame.setMedico(medico);
        }

        if (!exame.getPaciente().getId().equals(dto.pacienteId())){
            PacienteResponseDTO pacienteResponseDTO = this.pacienteService.findById(dto.pacienteId());
            Paciente paciente = PacienteMapper.toEntityFromDto(pacienteResponseDTO);
            exame.setPaciente(paciente);
        }

        exame.setNome(dto.nome());
        exame.setDataHora(dto.dataHora());
        exame.setDescricao(dto.descricao());

        this.exameRepository.save(exame);

        return ExameMapper.toDtoFromEntity(exame);
    }

    public void delete(Long id){
        findById(id);

        try{
            this.exameRepository.deleteById(id);
        }
        catch(DataIntegrityViolationException exception){
            throw new DataIntegrityViolationException("Não é possível excluir, pois o exame possui vinculações");
        }
    }
}
