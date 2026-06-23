package com.TrabalhoBD.clinica.validation.exame;

import com.TrabalhoBD.clinica.models.Exame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CadeiaValidacaoExame {

    @Autowired
    private ValidadorExameMedicoPacienteObrigatorio validadorObrigatorio;

    @Autowired
    private ValidadorExameDataHoraFutura validadorDataHora;

    @Autowired
    private ValidadorExameConflitoAgendaMedico validadorConflitoMedico;

    @Autowired
    private ValidadorExameConflitoAgendaPaciente validadorConflitoPaciente;

    public void validar(Exame exame) {
        validadorObrigatorio.setProximo(validadorDataHora);
        validadorDataHora.setProximo(validadorConflitoMedico);
        validadorConflitoMedico.setProximo(validadorConflitoPaciente);
        validadorConflitoPaciente.setProximo(null);

        validadorObrigatorio.validar(exame);
    }
}
