package com.TrabalhoBD.clinica.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.TrabalhoBD.clinica.exceptions.NotFoundException;
import com.TrabalhoBD.clinica.models.Receita;
import com.TrabalhoBD.clinica.repositories.ReceitaRepository;


@Service
public class ReceitaService {
    
    @Autowired
    private ReceitaRepository receitaRepository;

    public Receita findById(Long id){
        Optional<Receita> receita = this.receitaRepository.findById(id);

        return receita.orElseThrow( () -> new NotFoundException("Receita de id = " + id + " não encontrada"));
    }

    public List<Receita> findAll(){
        List<Receita> list = this.receitaRepository.findAll();

        if (list.isEmpty()){
            throw new NotFoundException("Nenhuma receita encontrada");
        }
        return list;
    }

    public List<Receita> findAllByConsultaId(Long consultaId){
        List<Receita> list = this.receitaRepository.findByConsulta_id(consultaId);
        if (list.isEmpty()){
            throw new NotFoundException("Nenhuma consulta encontrada");
        }
        return list;
    }

    @Transactional
    public void create(Receita receita){
        if (receita.getConsulta() != null && receita.getConsulta().getId() != null) {
            receita.setDataEmissao(receita.getConsulta().getDataHora());
        }
        this.receitaRepository.save(receita);
    }

    @Transactional
    public Receita update(Receita receita){
        Receita newReceita = findById(receita.getId());

        newReceita.setMedicamento(receita.getMedicamento());
        newReceita.setDosagem(receita.getDosagem());
        newReceita.setInstrucoes(receita.getInstrucoes());

        if (receita.getDataEmissao() != null){
            newReceita.setDataEmissao(receita.getDataEmissao());
        }

        if (receita.getConsulta() != null && receita.getConsulta().getId() != null) {
            newReceita.setConsulta(receita.getConsulta());
        }
        return this.receitaRepository.save(newReceita);
    }

    public void delete(Long id){
        findById(id);

        try{
            this.receitaRepository.deleteById(id);
        }
        catch(DataIntegrityViolationException exception){
            throw new DataIntegrityViolationException("Não é possível excluir, pois o receita possui vinculações");
        }
    }
}
