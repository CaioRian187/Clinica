package com.TrabalhoBD.clinica.services;

import java.time.LocalDateTime;
import java.util.List;

import com.TrabalhoBD.clinica.dtos.ConsultaRequestDTO;
import com.TrabalhoBD.clinica.dtos.ConsultaResponseDTO;
import com.TrabalhoBD.clinica.dtos.MedicoResponseDTO;
import com.TrabalhoBD.clinica.dtos.PacienteResponseDTO;
import com.TrabalhoBD.clinica.mapper.ConsultaMapper;
import com.TrabalhoBD.clinica.mapper.MedicoMapper;
import com.TrabalhoBD.clinica.mapper.PacienteMapper;
import com.TrabalhoBD.clinica.models.Medico;
import com.TrabalhoBD.clinica.models.Paciente;
import com.TrabalhoBD.clinica.validation.consulta.CadeiaValidacaoConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.TrabalhoBD.clinica.models.Consulta;
import com.TrabalhoBD.clinica.repositories.ConsultaRepository;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ConsultaService {
    
    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoService medicoService;

    // GoF — Chain of Responsibility: cadeia de validação injetada pelo Spring
    @Autowired
    private CadeiaValidacaoConsulta cadeiaValidacao;

    @Autowired
    private PacienteService pacienteService;

    public ConsultaResponseDTO findById(Long id){
        Consulta consulta = this.consultaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Consulta de Id: " + id + " não encontrada."
                ));
        return ConsultaMapper.toDtoFromEntity(consulta);
    }

    public List<ConsultaResponseDTO> findAllByMedicoId(Long medicoId){
        List<Consulta> consultas = this.consultaRepository.findByMedico_id(medicoId);

        this.verificarListaVazia(consultas);

        return consultas.stream().map(ConsultaMapper::toDtoFromEntity).toList();
    }

    public List<ConsultaResponseDTO> findAllByPacienteId(Long pacienteId){
        List<Consulta> consultas = this.consultaRepository.findByPaciente_id(pacienteId);

        this.verificarListaVazia(consultas);

        return consultas.stream().map(ConsultaMapper::toDtoFromEntity).toList();
    }

    public List<ConsultaResponseDTO> findAll(){
        List<Consulta> consultas = this.consultaRepository.findAll();

        this.verificarListaVazia(consultas);

        return consultas.stream().map(ConsultaMapper::toDtoFromEntity).toList();
    }

    private void verificarListaVazia(List<Consulta> consultas){
        if (consultas == null ||consultas.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Nenhuma consulta agendada."
            );
        }
    }

    @Transactional
    public ConsultaResponseDTO createConsulta(ConsultaRequestDTO dto){

        MedicoResponseDTO medicoResponseDTO = this.medicoService.findById(dto.medicoId());
        Medico medico = MedicoMapper.toEntityFromDto(medicoResponseDTO);

        PacienteResponseDTO pacienteResponseDTO = this.pacienteService.findById(dto.pacienteId());
        Paciente paciente = PacienteMapper.toEntityFromDto(pacienteResponseDTO);

        //this.validarDataHora(dto.datahora());

        Consulta consulta = new Consulta.ConsultaBuilder()
                .adicionarDataHora(dto.datahora())
                .adicionarObservacoes(dto.observacoes())
                .adicionarMedico(medico)
                .adicionarPaciente(paciente)
                .build();

        // GoF — Chain of Responsibility: dispara a cadeia antes de salvar
        cadeiaValidacao.validar(consulta);

        this.consultaRepository.save(consulta);

        return ConsultaMapper.toDtoFromEntity(consulta);
    }

    private void validarDataHora(LocalDateTime dateHora){

        if (this.consultaRepository.existsByDataHora(dateHora)){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Não é possível cadastrar essa consulta, já existe uma consulta cadastrada nessa data e horário."
            );
        }
    }

    @Transactional
    public ConsultaResponseDTO updateConsulta(Long id, ConsultaRequestDTO dto){
        Consulta consulta = this.consultaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Consulta de Id: " + id + " não encontrada."
                ));

        if (!consulta.getMedico().getId().equals(dto.medicoId())){
            MedicoResponseDTO medicoResponseDTO = this.medicoService.findById(dto.medicoId());
            Medico medico = MedicoMapper.toEntityFromDto(medicoResponseDTO);
            consulta.setMedico(medico);
        }

        if (!consulta.getPaciente().getId().equals(dto.pacienteId())){
            PacienteResponseDTO pacienteResponseDTO = this.pacienteService.findById(dto.pacienteId());
            Paciente paciente = PacienteMapper.toEntityFromDto(pacienteResponseDTO);
            consulta.setPaciente(paciente);
        }

        consulta.setDataHora(dto.datahora());
        consulta.setObservacoes(dto.observacoes());

        // GoF — Chain of Responsibility: revalida o novo horário ao atualizar
        cadeiaValidacao.validar(consulta);

        this.consultaRepository.save(consulta);

        return ConsultaMapper.toDtoFromEntity(consulta);
    }

    public void deleteConsulta(Long id){
        findById(id);

        try{
            this.consultaRepository.deleteById(id);
        }
        catch(DataIntegrityViolationException exception){
            throw new DataIntegrityViolationException("Não é possível excluir, pois a consulta possui vinculações");
        }
    }



}
