package com.TrabalhoBD.clinica.validation.consulta;

import com.TrabalhoBD.clinica.exceptionHandler.exception.ValidacaoAgendamentoException;
import com.TrabalhoBD.clinica.models.Consulta;
import org.springframework.stereotype.Component;

/**
 * GoF — Chain of Responsibility  |  Elo 1
 *
 * Regra: médico e paciente são obrigatórios.
 * Bloqueia consultas criadas sem associação com nenhuma das entidades.
 */
@Component
public class ValidadorMedicoPacienteObrigatorio extends ValidadorConsultaBase {

    @Override
    public void validar(Consulta consulta) {
        if (consulta.getMedico() == null || consulta.getMedico().getId() == null) {
            throw new ValidacaoAgendamentoException(
                "Não é possível agendar uma consulta sem informar o médico responsável.");
        }
        if (consulta.getPaciente() == null || consulta.getPaciente().getId() == null) {
            throw new ValidacaoAgendamentoException(
                "Não é possível agendar uma consulta sem informar o paciente.");
        }
        delegarProximo(consulta);
    }
}
