package com.TrabalhoBD.clinica.validation.consulta;

import com.TrabalhoBD.clinica.exceptionHandler.exception.ValidacaoAgendamentoException;
import com.TrabalhoBD.clinica.models.Consulta;
import com.TrabalhoBD.clinica.models.Exame;
import com.TrabalhoBD.clinica.repositories.ConsultaRepository;
import com.TrabalhoBD.clinica.repositories.ExameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ValidadorConflitoAgendaMedico extends ValidadorConsultaBase {

    private static final int DURACAO_CONSULTA_MINUTOS = 30;
    private static final int DURACAO_EXAME_MINUTOS = 60;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private ExameRepository exameRepository;

    @Override
    public void validar(Consulta consulta) {
        LocalDateTime inicio = consulta.getDataHora();
        LocalDateTime fim = inicio.plusMinutes(DURACAO_CONSULTA_MINUTOS);

        List<Consulta> consultasMedico =
            consultaRepository.findByMedico_id(consulta.getMedico().getId());

        boolean conflitoConsulta = consultasMedico.stream()
            .filter(c -> !isMesmaConsulta(c, consulta))
            .anyMatch(c -> {
                LocalDateTime existenteInicio = c.getDataHora();
                LocalDateTime existenteFim = existenteInicio.plusMinutes(DURACAO_CONSULTA_MINUTOS);
                return existeSobreposicao(inicio, fim, existenteInicio, existenteFim);
            });

        List<Exame> examesMedico =
            exameRepository.findByMedico_id(consulta.getMedico().getId());

        boolean conflitoExame = examesMedico.stream()
            .anyMatch(e -> {
                LocalDateTime existenteInicio = e.getDataHora();
                LocalDateTime existenteFim = existenteInicio.plusMinutes(DURACAO_EXAME_MINUTOS);
                return existeSobreposicao(inicio, fim, existenteInicio, existenteFim);
            });

        if (conflitoConsulta || conflitoExame) {
            throw new ValidacaoAgendamentoException(
                "O medico ja possui um agendamento nesse horario. "
                + "Por favor, escolha outro horario.");
        }

        delegarProximo(consulta);
    }

    private boolean isMesmaConsulta(Consulta existente, Consulta atual) {
        return atual.getId() != null && atual.getId().equals(existente.getId());
    }

    private boolean existeSobreposicao(
            LocalDateTime inicio,
            LocalDateTime fim,
            LocalDateTime existenteInicio,
            LocalDateTime existenteFim) {
        return inicio.isBefore(existenteFim) && fim.isAfter(existenteInicio);
    }
}
