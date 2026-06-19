package com.TrabalhoBD.clinica.controllers;

import java.net.URI;
import java.util.List;

import com.TrabalhoBD.clinica.dtos.ConsultaRequestDTO;
import com.TrabalhoBD.clinica.dtos.ConsultaResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.TrabalhoBD.clinica.models.Consulta;
import com.TrabalhoBD.clinica.services.ConsultaService;
import com.TrabalhoBD.clinica.services.MedicoService;
import com.TrabalhoBD.clinica.services.PacienteService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/consulta")
@Validated
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    @Autowired
    private MedicoService medicoService;

    @Autowired
    private PacienteService pacienteService;

    @GetMapping("/{id}")
    public ResponseEntity<ConsultaResponseDTO> findById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(this.consultaService.findById(id));
    }

    @GetMapping("/medico/{id_medico}")
    public ResponseEntity<List<Consulta>> findAllByMedicoId(@PathVariable Long id_medico){
        this.medicoService.findById(id_medico);
        List<Consulta> consultas = this.consultaService.findAllByMedicoId(id_medico);
        return ResponseEntity.ok().body(consultas);
    }

    @GetMapping("/paciente/{id_paciente}")
    public ResponseEntity<List<Consulta>> findAllByPacienteId(@PathVariable Long id_paciente){
        this.pacienteService.findById(id_paciente);
        List<Consulta> consultas = this.consultaService.findAllByPacienteId(id_paciente);
        return ResponseEntity.ok().body(consultas);
    }

    @GetMapping
    public ResponseEntity<List<Consulta>> findAll(){
        List<Consulta> list = this.consultaService.findAll();

        return ResponseEntity.ok().body(list);
    }

    @PostMapping()
    public ResponseEntity<ConsultaResponseDTO> createConsulta(@Valid @RequestBody ConsultaRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.consultaService.createConsulta(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsultaResponseDTO> updateConsulta(@PathVariable Long id, @Valid @RequestBody ConsultaRequestDTO dto ){
        return ResponseEntity.status(HttpStatus.OK).body(this.consultaService.updateConsulta(id, dto));
    } 

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsulta(@PathVariable Long id){
        this.consultaService.deleteConsulta(id);
        return ResponseEntity.noContent().build();
    }
}
