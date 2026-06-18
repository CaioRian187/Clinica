package com.TrabalhoBD.clinica.controllers;

import java.net.URI;
import java.util.List;

import com.TrabalhoBD.clinica.dtos.EspecialidadeRequestDTO;
import com.TrabalhoBD.clinica.dtos.EspecialidadeResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.TrabalhoBD.clinica.models.Especialidade;
import com.TrabalhoBD.clinica.services.EspecialidadeService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/especialidade")
@Validated
public class EspecialidadeController {

    @Autowired
    private EspecialidadeService especialidadeService;

    @GetMapping("/{id}")
    public ResponseEntity<Especialidade> findById(@PathVariable long id){
        Especialidade especialidade = this.especialidadeService.findById(id);
        return ResponseEntity.ok().body(especialidade);
    }

    @GetMapping("nome/{nome}")
    public ResponseEntity<Especialidade> findByNome(@Valid @PathVariable String nome){
        Especialidade especialidade = this.especialidadeService.findByNome(nome);
        return ResponseEntity.ok().body(especialidade);
    }

    @GetMapping
    public ResponseEntity<List<Especialidade>> findAll(){
        List<Especialidade> list = this.especialidadeService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping
    public ResponseEntity<EspecialidadeResponseDTO> create(@Valid @RequestBody EspecialidadeRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.especialidadeService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody Especialidade especialidade, @PathVariable long id){
        especialidade.setId(id);
        especialidade = this.especialidadeService.update(especialidade);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id){
        this.especialidadeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}