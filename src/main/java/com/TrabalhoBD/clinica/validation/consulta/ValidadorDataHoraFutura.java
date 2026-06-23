package com.TrabalhoBD.clinica.validation.consulta;

import com.TrabalhoBD.clinica.exceptionHandler.exception.ValidacaoAgendamentoException;
import com.TrabalhoBD.clinica.models.Consulta;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * GoF — Chain of Responsibility  |  Elo 2
 *
 * Regra: a data/hora da consulta deve estar no futuro.
 * Impede o cadastro de consultas com datas retroativas.
 */
@Component
public class ValidadorDataHoraFutura extends ValidadorConsultaBase {

    @Override
    public void validar(Consulta consulta) {
        if (consulta.getDataHora() == null) {
            throw new ValidacaoAgendamentoException(
                "A data e hora da consulta são obrigatórias.");
        }
        if (!consulta.getDataHora().isAfter(LocalDateTime.now())) {
            throw new ValidacaoAgendamentoException(
                "Não é possível agendar uma consulta para uma data/hora que já passou. "
                + "Data informada: " + consulta.getDataHora());
        }
        delegarProximo(consulta);
    }
}
