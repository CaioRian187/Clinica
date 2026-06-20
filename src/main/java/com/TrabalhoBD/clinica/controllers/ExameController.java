package com.TrabalhoBD.clinica.controllers;

import java.util.List;

import com.TrabalhoBD.clinica.dtos.ExameRequestDTO;
import com.TrabalhoBD.clinica.dtos.ExameResponseDTO;
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

import com.TrabalhoBD.clinica.services.ExameService;
import com.TrabalhoBD.clinica.services.MedicoService;
import com.TrabalhoBD.clinica.services.PacienteService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/exame")
@Validated
public class ExameController {
    
    @Autowired
    private ExameService exameService;

    @Autowired
    private MedicoService medicoService;

    @Autowired
    private PacienteService pacienteService;

    @GetMapping("/{id}")
    public ResponseEntity<ExameResponseDTO> findById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(this.exameService.findById(id));
    }


    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<List<ExameResponseDTO>> findByMedicoId(@PathVariable Long medicoId){
        return ResponseEntity.status(HttpStatus.OK).body(this.exameService.findByMedicoid(medicoId));
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<ExameResponseDTO>> findByPacienteId(@PathVariable Long pacienteId){
        return ResponseEntity.status(HttpStatus.OK).body(this.exameService.findByPacienteId(pacienteId));
    }

    @GetMapping
    public ResponseEntity<List<ExameResponseDTO>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(this.exameService.findAll());
    }


    @PostMapping
    public ResponseEntity<ExameResponseDTO> create(@Valid @RequestBody ExameRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.exameService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExameResponseDTO> update(@PathVariable Long id, @Valid @RequestBody ExameRequestDTO dto ){
        return ResponseEntity.status(HttpStatus.OK).body(this.exameService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        this.exameService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
