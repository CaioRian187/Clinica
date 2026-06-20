package com.TrabalhoBD.clinica.services;

import java.util.List;
import java.util.Optional;

import com.TrabalhoBD.clinica.dtos.EspecialidadeRequestDTO;
import com.TrabalhoBD.clinica.dtos.EspecialidadeResponseDTO;
import com.TrabalhoBD.clinica.mapper.EspecialidadeMapper;
import com.TrabalhoBD.clinica.models.Receita;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.TrabalhoBD.clinica.models.Especialidade;
import com.TrabalhoBD.clinica.repositories.EspecialidadeRepository;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EspecialidadeService {

    @Autowired
    private EspecialidadeRepository especialidadeRepository;

    public EspecialidadeResponseDTO findById(long id){
        Especialidade especialidade = this.especialidadeRepository.findById(id)
                .orElseThrow( () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Especialidade de Id: " + id + " não encontrada."
                ));
        return EspecialidadeMapper.toDtoFromEntity(especialidade);
    }

    public EspecialidadeResponseDTO findByNome(String nome){
        Especialidade especialidade = this.especialidadeRepository.findByNome(nome)
                .orElseThrow( () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Especialidade de Nome: " + nome + " não encontrada."
                ));
        return EspecialidadeMapper.toDtoFromEntity(especialidade);
    }

    public List<EspecialidadeResponseDTO> findAll(){
        List<Especialidade> especialidades = this.especialidadeRepository.findAll();

        verificarListaVazia(especialidades);

        return especialidades.stream().map(EspecialidadeMapper::toDtoFromEntity).toList();
    }


    private void verificarListaVazia(List<Especialidade> especialidades){
        if (especialidades == null || especialidades.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Nenhuma especialidade encontrada."
            );
        }
    }

    @Transactional
    public EspecialidadeResponseDTO create(EspecialidadeRequestDTO dto){
        Especialidade especialidade = new Especialidade();
        especialidade.setNome(dto.nome());

        this.especialidadeRepository.save(especialidade);

        return EspecialidadeMapper.toDtoFromEntity(especialidade);
    }

    @Transactional
    public EspecialidadeResponseDTO update(Long id, EspecialidadeRequestDTO dto){

        EspecialidadeResponseDTO especialidadeResponseDTO = this.findById(id);
        Especialidade especialidade = EspecialidadeMapper.toEntityFromDto(especialidadeResponseDTO);

        especialidade.setNome(dto.nome());

        this.especialidadeRepository.save(especialidade);

        return EspecialidadeMapper.toDtoFromEntity(especialidade);
    }

    public void delete(long id){
        findById(id);
        try {
            this.especialidadeRepository.deleteById(id);
        } catch (DataIntegrityViolationException exception) {
            throw new DataIntegrityViolationException("Não é possível excluir, pois a especialidade possui vinculações");
        }
    }
}