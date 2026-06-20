package com.TrabalhoBD.clinica.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.TrabalhoBD.clinica.models.Especialidade;

public interface EspecialidadeRepository extends JpaRepository<Especialidade,Long> {
    Optional<Especialidade> findByNome(String nome);
}