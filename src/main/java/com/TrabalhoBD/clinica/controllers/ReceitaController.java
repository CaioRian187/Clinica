package com.TrabalhoBD.clinica.controllers;

import java.util.List;

import com.TrabalhoBD.clinica.dtos.ReceitaRequestDTO;
import com.TrabalhoBD.clinica.dtos.ReceitaResponseDTO;
import com.TrabalhoBD.clinica.repositories.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import com.TrabalhoBD.clinica.services.ConsultaService;
import com.TrabalhoBD.clinica.services.ReceitaService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/receita")
@Validated
public class ReceitaController {
    
    @Autowired
    private ReceitaService receitaService;

    @Autowired
    private ConsultaService consultaService;

    @Autowired
    private ConsultaRepository consultaRepository;

    @GetMapping("/{id}")
    public ResponseEntity<ReceitaResponseDTO> findById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(this.receitaService.findById(id));
    }

    @GetMapping("/consulta/{consultaId}")
    public ResponseEntity<List<ReceitaResponseDTO>> findByConsultaId(@PathVariable Long consultaId){
        return ResponseEntity.status(HttpStatus.OK).body(this.receitaService.findAllByConsultaId(consultaId));
    }

    @GetMapping
    public ResponseEntity<List<ReceitaResponseDTO>> findAllReceitas(){
        return ResponseEntity.status(HttpStatus.OK).body(this.receitaService.findAll());
    }

    @PostMapping
    public ResponseEntity<ReceitaResponseDTO> createReceita(@Valid @RequestBody ReceitaRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.receitaService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReceitaResponseDTO> updateReceita( @PathVariable Long id, @Valid @RequestBody ReceitaRequestDTO dto){
        return ResponseEntity.status(HttpStatus.OK).body(this.receitaService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReceita(@PathVariable Long id){
        this.receitaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
