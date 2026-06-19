package com.TrabalhoBD.clinica.controllers;

import java.net.URI;
import java.util.List;

import com.TrabalhoBD.clinica.dtos.ConsultaResponseDTO;
import com.TrabalhoBD.clinica.dtos.ReceitaRequestDTO;
import com.TrabalhoBD.clinica.mapper.ConsultaMapper;
import com.TrabalhoBD.clinica.repositories.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.TrabalhoBD.clinica.models.Consulta;
import com.TrabalhoBD.clinica.models.Receita;
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
    public ResponseEntity<Receita> findById(@PathVariable Long id){
        Receita receita = this.receitaService.findById(id);
        return ResponseEntity.ok().body(receita);
    }

    @GetMapping("/consulta/{consulta_id}")
    public ResponseEntity<List<Receita>> findByConsultaId(@PathVariable Long consulta_id){
        this.consultaService.findById(consulta_id);
        List<Receita> list = this.receitaService.findAllByConsultaId(consulta_id);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping
    public ResponseEntity<List<Receita>> findAllReceitas(){
        List<Receita> list = this.receitaService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping
    public ResponseEntity<Void> createReceita(@Valid @RequestBody ReceitaRequestDTO dto){
        Consulta consulta = this.consultaRepository.findById(dto.consultaId())
                .orElseThrow( () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Consulta de Id: " + dto.consultaId() + " não encontrada."
                ));

        Receita receita = new Receita();

        receita.setConsulta(consulta);
        receita.setDataEmissao(consulta.getDataHora());

        this.receitaService.create(receita);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(receita.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateReceita(@Valid @RequestBody Receita receita, @PathVariable Long id){
        receita.setId(id);

        if (receita.getDataEmissao() == null && receita.getConsulta() != null) {
            Consulta c = this.consultaRepository.findById(receita.getConsulta().getId())
                    .orElseThrow( () -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Consulta de não encontrada."
                    ));
            receita.setDataEmissao(c.getDataHora());
        }
        
        receita = this.receitaService.update(receita);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReceita(@PathVariable Long id){
        this.receitaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
