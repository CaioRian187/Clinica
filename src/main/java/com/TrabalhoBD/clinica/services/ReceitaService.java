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
import com.TrabalhoBD.clinica.repositories.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.TrabalhoBD.clinica.exceptions.NotFoundException;
import com.TrabalhoBD.clinica.models.Receita;
import com.TrabalhoBD.clinica.repositories.ReceitaRepository;
import org.springframework.web.server.ResponseStatusException;


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

    @Autowired
    private ConsultaRepository consultaRepository;

    public ReceitaResponseDTO findById(Long id){
        Receita receita = this.receitaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Receita de Id: " + id + " não encontrada."
                ));
        return ReceitaMapper.toDtoFromEntity(receita);
    }

    public List<ReceitaResponseDTO> findAll(){
        List<Receita> list = this.receitaRepository.findAll();

        verificarListaVazia(list);

        return list.stream().map(ReceitaMapper::toDtoFromEntity).toList();
    }

    public List<ReceitaResponseDTO> findAllByConsultaId(Long consultaId){
        List<Receita> list = this.receitaRepository.findByConsulta_id(consultaId);

        verificarListaVazia(list);

        return list.stream().map(ReceitaMapper::toDtoFromEntity).toList();
    }

    private void verificarListaVazia(List<Receita> receitas){
        if (receitas == null ||receitas.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Nenhuma consulta agendada."
            );
        }
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
    public ReceitaResponseDTO update(Long id, ReceitaRequestDTO dto){
        Consulta consulta = this.consultaRepository.findById(dto.consultaId())
                .orElseThrow( () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Consulta não encontrada"
                ));

        ReceitaResponseDTO receitaResponseDTO = this.findById(id);
        Receita receita = ReceitaMapper.toEntityFromDto(receitaResponseDTO, consulta);

        receita.setMedicamento(dto.medicamento());
        receita.setDosagem(dto.dosagem());
        receita.setInstrucoes(dto.instrucoes());

        this.receitaRepository.save(receita);

        return ReceitaMapper.toDtoFromEntity(receita);
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
