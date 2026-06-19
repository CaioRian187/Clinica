package com.TrabalhoBD.clinica.controllers;

import java.util.List;

import com.TrabalhoBD.clinica.dtos.PacienteRequestDTO;
import com.TrabalhoBD.clinica.dtos.PacienteResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.TrabalhoBD.clinica.models.Paciente;
import com.TrabalhoBD.clinica.services.PacienteService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/paciente")
@Validated
public class PacienteController {
    
    @Autowired
    private PacienteService pacienteService;

    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok().body(this.pacienteService.findById(id));
    }

    @GetMapping("nome/{nome}")
    public ResponseEntity<PacienteResponseDTO> findByNome(@Valid @PathVariable String nome){
        return ResponseEntity.ok().body(this.pacienteService.findByNome(nome));
    } 

    @GetMapping
    public ResponseEntity<List<PacienteResponseDTO>> findAll(){
        return ResponseEntity.ok().body(this.pacienteService.findAll());
    }

    @PostMapping
    public ResponseEntity<PacienteResponseDTO> createPaciente(@Valid @RequestBody PacienteRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.pacienteService.createPaciente(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> updatePaciente(@PathVariable Long id, @Valid @RequestBody PacienteRequestDTO dto ){
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(this.pacienteService.updatePaciente(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaciente(@PathVariable Long id){
        this.pacienteService.deletePaciente(id);
        return ResponseEntity.noContent().build();
    }
}
