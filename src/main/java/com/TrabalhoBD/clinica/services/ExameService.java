package com.TrabalhoBD.clinica.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.TrabalhoBD.clinica.exceptions.NotFoundException;
import com.TrabalhoBD.clinica.models.Exame;
import com.TrabalhoBD.clinica.repositories.ExameRepository;

@Service
public class ExameService {
    
    @Autowired
    private ExameRepository exameRepository;

    public Exame findById(Long id){
        Optional<Exame> exame = this.exameRepository.findById(id);
        return exame.orElseThrow( () -> new NotFoundException("Exame de id = "+ id +" não encontrado"));
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
    public void create(Exame exame){
        this.exameRepository.save(exame);
    }


    @Transactional
    public Exame update(Exame exame){
        Exame newExame = this.findById(exame.getId());

        newExame.setNome(exame.getNome());
        newExame.setDataHora(exame.getDataHora());
        newExame.setDescricao(exame.getDescricao());

        return this.exameRepository.save(newExame);
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
