package com.TrabalhoBD.clinica.controllers;

import java.net.URI;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.TrabalhoBD.clinica.models.Exame;
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


    @GetMapping("/medico/{id_medico}")
    public ResponseEntity<List<Exame>> findByMedicoId(@PathVariable Long id_medico){
        this.medicoService.findById(id_medico);

        List<Exame> exames = this.exameService.findByMedicoid(id_medico);
        return ResponseEntity.ok().body(exames);
    }

    @GetMapping("/paciente/{id_paciente}")
    public ResponseEntity<List<Exame>> findByPacienteId(@PathVariable Long id_paciente){
        this.pacienteService.findById(id_paciente);
        List<Exame> exames = this.exameService.findByPacienteId(id_paciente);
        return ResponseEntity.ok().body(exames);
    }

    @GetMapping
    public ResponseEntity<List<Exame>> findAll(){
        List<Exame> list = this.exameService.findAll();
        return ResponseEntity.ok().body(list);
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
