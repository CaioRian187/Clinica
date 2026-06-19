package com.TrabalhoBD.clinica.services;

import java.util.List;
import java.util.Optional;

import com.TrabalhoBD.clinica.dtos.*;
import com.TrabalhoBD.clinica.mapper.ConsultaMapper;
import com.TrabalhoBD.clinica.mapper.MedicoMapper;
import com.TrabalhoBD.clinica.mapper.PacienteMapper;
import com.TrabalhoBD.clinica.mapper.ReceitaMapper;
import com.TrabalhoBD.clinica.models.Consulta;
import com.TrabalhoBD.clinica.models.Medico;
import com.TrabalhoBD.clinica.models.Paciente;
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

    @Autowired
    private ConsultaService consultaService;

    @Autowired
    private MedicoService medicoService;

    @Autowired
    private PacienteService pacienteService;

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
    public ReceitaResponseDTO create(ReceitaRequestDTO dto){

        ConsultaResponseDTO consultaResponseDTO = this.consultaService.findById(dto.consultaId());

        MedicoResponseDTO medicoResponseDTO = this.medicoService.findById(consultaResponseDTO.medicoId());
        Medico medico = MedicoMapper.toEntityFromDto(medicoResponseDTO);

        PacienteResponseDTO pacienteResponseDTO = this.pacienteService.findById(consultaResponseDTO.pacienteId());
        Paciente paciente = PacienteMapper.toEntityFromDto(pacienteResponseDTO);

        Consulta consulta = ConsultaMapper.toEntityFromDto(consultaResponseDTO, medico, paciente);

        Receita receita = new Receita.ReceitaBuilder()
                .adicionarDataEmissao(dto.dataEmissao())
                .adicionarMedicamento(dto.medicamento())
                .adicionarDosagem(dto.dosagem())
                .adicionarInstrucoes(dto.instrucoes())
                .adicionarConsulta(consulta)
                .build();

        this.receitaRepository.save(receita);

        return ReceitaMapper.toDtoFromEntity(receita);
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
