package com.TrabalhoBD.clinica.controllers;

import java.util.List;

import com.TrabalhoBD.clinica.dtos.ConsultaRequestDTO;
import com.TrabalhoBD.clinica.dtos.ConsultaResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.TrabalhoBD.clinica.services.ConsultaService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/consulta")
@Validated
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    @GetMapping("/{id}")
    public ResponseEntity<ConsultaResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.consultaService.findById(id));
    }

    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<List<ConsultaResponseDTO>> findAllByMedicoId(@PathVariable Long medicoId) {
        return ResponseEntity.status(HttpStatus.OK).body(this.consultaService.findAllByMedicoId(medicoId));
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<ConsultaResponseDTO>> findAllByPacienteId(@PathVariable Long pacienteId) {
        return ResponseEntity.status(HttpStatus.OK).body(this.consultaService.findAllByPacienteId(pacienteId));
    }

    @GetMapping
    public ResponseEntity<List<ConsultaResponseDTO>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(this.consultaService.findAll());
    }

    @PostMapping()
    public ResponseEntity<ConsultaResponseDTO> createConsulta(@Valid @RequestBody ConsultaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.consultaService.createConsulta(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsultaResponseDTO> updateConsulta(@PathVariable Long id,
            @Valid @RequestBody ConsultaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(this.consultaService.updateConsulta(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsulta(@PathVariable Long id) {
        this.consultaService.deleteConsulta(id);
        return ResponseEntity.noContent().build();
    }
}
