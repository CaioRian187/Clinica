package com.TrabalhoBD.clinica.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.TrabalhoBD.clinica.models.Consulta;

public interface ConsultaRepository extends JpaRepository<Consulta, Long>{

    List<Consulta> findByMedico_id(Long id);
    List<Consulta> findByPaciente_id(Long id);

    // 3. COUNT + WHERE: Conta quantas consultas um médico específico tem
    @Query("SELECT COUNT(c) FROM Consulta c WHERE c.medico.id = :medicoId")
    Long countConsultasPorMedico(@Param("medicoId") Long medicoId);

    // 4. MAX: Retorna a data/hora da última consulta registrada no sistema
    @Query("SELECT MAX(c.dataHora) FROM Consulta c")
    LocalDateTime findUltimaConsultaData();

    // 5. JOIN: Busca consultas filtrando pelo NOME da Especialidade do médico
    // Isso faz um JOIN entre Consulta -> Médico -> Especialidade
    @Query("SELECT c FROM Consulta c JOIN c.medico m JOIN m.especialidades e WHERE e.nome = :nomeEspecialidade")
    List<Consulta> findByEspecialidadeNome(@Param("nomeEspecialidade") String nomeEspecialidade);

    boolean existsByDataHora(LocalDateTime dateHora);

}