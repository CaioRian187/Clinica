package com.TrabalhoBD.clinica.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.TrabalhoBD.clinica.dtos.EspecialidadeResponseDTO;
import com.TrabalhoBD.clinica.dtos.MedicoRequestDTO;
import com.TrabalhoBD.clinica.dtos.MedicoResponseDTO;
import com.TrabalhoBD.clinica.models.Especialidade;
import com.TrabalhoBD.clinica.repositories.EspecialidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
//import org.springframework.dao.DataIntegrityViolationException;


import com.TrabalhoBD.clinica.exceptions.NotFoundException;
import com.TrabalhoBD.clinica.models.Medico;
import com.TrabalhoBD.clinica.repositories.MedicoRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private EspecialidadeRepository especialidadeRepository;

    public Medico findById(Long id){
        Optional<Medico> medico = this.medicoRepository.findById(id);
        return medico.orElseThrow(() -> new NotFoundException("Médico de id = " + id + " não encontrado"));
    }

    public Medico findByNome(String nome){
        return medicoRepository.findByNome(nome).orElseThrow(() -> new NotFoundException("Médico não encontrado"));
    }

    public List<Medico> findAllMedicos (){
        List<Medico> list = this.medicoRepository.findAll();
        if (list.isEmpty()){
            throw new NotFoundException("Médico não encontrado");
        }
        return list;
    }

    @Transactional
    public MedicoResponseDTO createMedico(MedicoRequestDTO dto){

        Medico novoMedico = new Medico.MedicoBuilder()
                .adicionarNome(dto.nome())
                .adicionarCrm(dto.crm())
                .adicionarTelefone(dto.telefone())
                .build();

        this.medicoRepository.save(novoMedico);

        return new MedicoResponseDTO(
                novoMedico.getId(),
                novoMedico.getNome(),
                novoMedico.getCrm(),
                novoMedico.getTelefone(),
                novoMedico.getEspecialidades().stream().map(
                                entity -> new EspecialidadeResponseDTO(
                                        entity.getId(),
                                        entity.getNome())
                                ).collect(Collectors.toSet()));
    }

    @Transactional
    public Medico updateMedico(Medico medico){
        Medico newMedico = findById(medico.getId());

        newMedico.setNome(medico.getNome());
        newMedico.setCrm(medico.getCrm());
        newMedico.setTelefone(medico.getTelefone());
        newMedico.setEspecialidades(resolveEspecialidades(medico));

        return this.medicoRepository.save(newMedico);
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
