package com.TrabalhoBD.clinica.controllers;

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
    public ResponseEntity<EspecialidadeResponseDTO> findById(@PathVariable long id){
        return ResponseEntity.status(HttpStatus.OK).body(this.especialidadeService.findById(id));
    }

    @GetMapping("nome/{nome}")
    public ResponseEntity<EspecialidadeResponseDTO> findByNome(@Valid @PathVariable String nome){
        return ResponseEntity.status(HttpStatus.OK).body(this.especialidadeService.findByNome(nome));
    }

    @GetMapping
    public ResponseEntity<List<EspecialidadeResponseDTO>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(this.especialidadeService.findAll());
    }

    @PostMapping
    public ResponseEntity<EspecialidadeResponseDTO> create(@Valid @RequestBody EspecialidadeRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.especialidadeService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EspecialidadeResponseDTO> update(@PathVariable Long id, @Valid @RequestBody EspecialidadeRequestDTO dto ){
        return ResponseEntity.status(HttpStatus.OK).body(this.especialidadeService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id){
        this.especialidadeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}