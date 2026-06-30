package com.TrabalhoBD.clinica.controllers;

import java.util.List;

import com.TrabalhoBD.clinica.dtos.MedicoRequestDTO;
import com.TrabalhoBD.clinica.dtos.MedicoResponseDTO;
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

import com.TrabalhoBD.clinica.services.MedicoService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*") // Adicione isto acima de @RestController
@RestController
@RequestMapping("/medico")
@Validated
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @GetMapping("/{id}")
    public ResponseEntity<MedicoResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(this.medicoService.findById(id));
    }

    @GetMapping("nome/{nome}")
    public ResponseEntity<MedicoResponseDTO> findByNome(@Valid @PathVariable String nome) {
        return ResponseEntity.ok().body(this.medicoService.findByNome(nome));
    }

    @GetMapping
    public ResponseEntity<List<MedicoResponseDTO>> findAllMedicos() {
        return ResponseEntity.ok().body(this.medicoService.findAllMedicos());
    }

    @PostMapping
    public ResponseEntity<MedicoResponseDTO> createMedico(@Valid @RequestBody MedicoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.medicoService.createMedico(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicoResponseDTO> updateMedico(@PathVariable Long id,
            @Valid @RequestBody MedicoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(this.medicoService.updateMedico(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedico(@PathVariable Long id) {
        this.medicoService.deleteMedico(id);
        return ResponseEntity.noContent().build();
    }

}
