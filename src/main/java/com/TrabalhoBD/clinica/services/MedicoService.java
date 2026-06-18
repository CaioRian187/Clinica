package com.TrabalhoBD.clinica.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.TrabalhoBD.clinica.dtos.AdicionarEspecialidadeRequestDTO;
import com.TrabalhoBD.clinica.dtos.EspecialidadeResponseDTO;
import com.TrabalhoBD.clinica.dtos.MedicoRequestDTO;
import com.TrabalhoBD.clinica.dtos.MedicoResponseDTO;
import com.TrabalhoBD.clinica.mapper.MedicoMapper;
import com.TrabalhoBD.clinica.models.Especialidade;
import com.TrabalhoBD.clinica.repositories.EspecialidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
//import org.springframework.dao.DataIntegrityViolationException;


import com.TrabalhoBD.clinica.exceptions.NotFoundException;
import com.TrabalhoBD.clinica.models.Medico;
import com.TrabalhoBD.clinica.repositories.MedicoRepository;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private EspecialidadeRepository especialidadeRepository;

    @Autowired
    private EspecialidadeService especialidadeService;

    public MedicoResponseDTO findById(Long id){
        Medico medico = this.medicoRepository.findById(id)
                .orElseThrow( () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Médico de Id: " + id + " não encontrado."
                ));

        return MedicoMapper.toDtoFromEntity(medico);
    }

    public MedicoResponseDTO findByNome(String nome){
        Medico medico = this.medicoRepository.findByNome(nome)
                .orElseThrow( () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Médico de nome: " + nome + " não encontrado."
                ));
        return MedicoMapper.toDtoFromEntity(medico);
    }

    public List<MedicoResponseDTO> findAllMedicos (){
        List<Medico> list = this.medicoRepository.findAll();
        if (list.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Nenhum médico encontrado."
            );
        }

        return list.stream().map(MedicoMapper::toDtoFromEntity).toList();
    }

    public MedicoResponseDTO adicionarEspecialidade(AdicionarEspecialidadeRequestDTO dto){
        Medico medico = this.medicoRepository.findById(dto.medicoId())
                .orElseThrow( () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Medico de Id: " + dto.medicoId() + " não encontrado."
                ));
        Especialidade especialidade = this.especialidadeRepository.findById(dto.especialidadeId())
                .orElseThrow( () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Especialidade de Id: " + dto.especialidadeId() + " não encontrado."
                ));

        medico.getEspecialidades().add(especialidade);

        this.medicoRepository.save(medico);

        return MedicoMapper.toDtoFromEntity(medico);
    }


    @Transactional
    public MedicoResponseDTO createMedico(MedicoRequestDTO dto){

        Medico novoMedico = new Medico.MedicoBuilder()
                .adicionarNome(dto.nome())
                .adicionarCrm(dto.crm())
                .adicionarTelefone(dto.telefone())
                .build();

        this.medicoRepository.save(novoMedico);

        return MedicoMapper.toDtoFromEntity(novoMedico);
    }

    @Transactional
    public MedicoResponseDTO updateMedico(Long id, MedicoRequestDTO dto){
        Medico medico = this.medicoRepository.findById(id)
                .orElseThrow( () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Médico de Id: " + id + " não encontrado."
                ));

        medico.setNome(dto.nome());
        medico.setCrm(dto.crm());
        medico.setTelefone(dto.telefone());

        this.medicoRepository.save(medico);

        return MedicoMapper.toDtoFromEntity(medico);
    }

    private Set<Especialidade> resolveEspecialidades(Medico medico) {
        if (medico.getEspecialidades() == null || medico.getEspecialidades().isEmpty()) {
            return new java.util.HashSet<>();
        }
        return medico.getEspecialidades().stream()
                .map(especialidade -> especialidadeRepository.findById(especialidade.getId())
                        .orElseThrow(() -> new NotFoundException("Especialidade de id = " + especialidade.getId() + " não encontrada")))
                .collect(java.util.stream.Collectors.toSet());
    }
    public void deleteMedico(Long id){
        findById(id);

        try {
            this.medicoRepository.deleteById(id);
        } catch (DataIntegrityViolationException exception) {
            throw new DataIntegrityViolationException("Não é possível excluir, pois o médico possui vinculações");
        }
    }
}
