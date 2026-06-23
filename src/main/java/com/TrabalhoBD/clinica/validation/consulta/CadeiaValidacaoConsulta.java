package com.TrabalhoBD.clinica.validation.consulta;

import com.TrabalhoBD.clinica.models.Consulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * GoF — Chain of Responsibility  |  Montador da Cadeia
 *
 * Responsável por encadear os validadores na ordem correta e expor
 * um único ponto de entrada: validar(consulta).
 *
 * Ordem da cadeia:
 *  [1] ValidadorMedicoPacienteObrigatorio
 *        → [2] ValidadorDataHoraFutura
 *              → [3] ValidadorConflitoAgendaMedico
 *                    → [4] ValidadorConflitoAgendaPaciente
 *
 * Para adicionar uma nova regra basta criar um novo ValidadorConsulta,
 * anotá-lo com @Component e encadeá-lo aqui — sem tocar em ConsultaService.
 */
@Component
public class CadeiaValidacaoConsulta {

    @Autowired
    private ValidadorMedicoPacienteObrigatorio validadorObrigatorio;

    @Autowired
    private ValidadorDataHoraFutura validadorDataHora;

    @Autowired
    private ValidadorConflitoAgendaMedico validadorAgendaMedico;

    @Autowired
    private ValidadorConflitoAgendaPaciente validadorAgendaPaciente;

    /**
     * Monta e dispara a cadeia completa para a consulta informada.
     * Lança ValidacaoAgendamentoException no primeiro elo que falhar.
     */
    public void validar(Consulta consulta) {
        // monta a cadeia
        validadorObrigatorio.setProximo(validadorDataHora);
        validadorDataHora.setProximo(validadorAgendaMedico);
        validadorAgendaMedico.setProximo(validadorAgendaPaciente);
        validadorAgendaPaciente.setProximo(null); // último elo

        // dispara pelo primeiro
        validadorObrigatorio.validar(consulta);
    }
}
