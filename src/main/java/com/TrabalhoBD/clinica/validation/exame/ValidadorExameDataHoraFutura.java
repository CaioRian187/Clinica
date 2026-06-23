package com.TrabalhoBD.clinica.validation.exame;

import com.TrabalhoBD.clinica.exceptionHandler.exception.ValidacaoAgendamentoException;
import com.TrabalhoBD.clinica.models.Exame;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * GoF — Chain of Responsibility  |  Elo 2 (Exame)
 *
 * Regra: a data/hora do exame deve estar no futuro.
 */
@Component
public class ValidadorExameDataHoraFutura extends ValidadorExameBase {

    @Override
    public void validar(Exame exame) {
        if (exame.getDataHora() == null) {
            throw new ValidacaoAgendamentoException(
                "A data e hora do exame são obrigatórias.");
        }
        if (!exame.getDataHora().isAfter(LocalDateTime.now())) {
            throw new ValidacaoAgendamentoException(
                "Não é possível agendar um exame para uma data/hora que já passou. "
                + "Data informada: " + exame.getDataHora());
        }
        delegarProximo(exame);
    }
}
