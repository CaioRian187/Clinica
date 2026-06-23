package com.TrabalhoBD.clinica.validation.exame;

import com.TrabalhoBD.clinica.exceptionHandler.exception.ValidacaoAgendamentoException;
import com.TrabalhoBD.clinica.models.Exame;
import org.springframework.stereotype.Component;

/**
 * GoF — Chain of Responsibility  |  Elo 1 (Exame)
 *
 * Regra: médico e paciente são obrigatórios no exame.
 */
@Component
public class ValidadorExameMedicoPacienteObrigatorio extends ValidadorExameBase {

    @Override
    public void validar(Exame exame) {
        if (exame.getMedico() == null || exame.getMedico().getId() == null) {
            throw new ValidacaoAgendamentoException(
                "Não é possível solicitar um exame sem informar o médico solicitante.");
        }
        if (exame.getPaciente() == null || exame.getPaciente().getId() == null) {
            throw new ValidacaoAgendamentoException(
                "Não é possível solicitar um exame sem informar o paciente.");
        }
        delegarProximo(exame);
    }
}
