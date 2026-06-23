package com.TrabalhoBD.clinica.validation.exame;

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
public class ValidadorExameConflitoAgendaMedico extends ValidadorExameBase {

    private static final int DURACAO_CONSULTA_MINUTOS = 30;
    private static final int DURACAO_EXAME_MINUTOS = 60;

    @Autowired
    private ExameRepository exameRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Override
    public void validar(Exame exame) {
        LocalDateTime inicio = exame.getDataHora();
        LocalDateTime fim = inicio.plusMinutes(DURACAO_EXAME_MINUTOS);

        List<Exame> examesMedico =
            exameRepository.findByMedico_id(exame.getMedico().getId());

        boolean conflitoExame = examesMedico.stream()
            .filter(e -> !isMesmoExame(e, exame))
            .anyMatch(e -> {
                LocalDateTime existenteInicio = e.getDataHora();
                LocalDateTime existenteFim = existenteInicio.plusMinutes(DURACAO_EXAME_MINUTOS);
                return existeSobreposicao(inicio, fim, existenteInicio, existenteFim);
            });

        List<Consulta> consultasMedico =
            consultaRepository.findByMedico_id(exame.getMedico().getId());

        boolean conflitoConsulta = consultasMedico.stream()
            .anyMatch(c -> {
                LocalDateTime existenteInicio = c.getDataHora();
                LocalDateTime existenteFim = existenteInicio.plusMinutes(DURACAO_CONSULTA_MINUTOS);
                return existeSobreposicao(inicio, fim, existenteInicio, existenteFim);
            });

        if (conflitoExame || conflitoConsulta) {
            throw new ValidacaoAgendamentoException(
                "O medico ja possui um agendamento nesse horario. "
                + "Por favor, escolha outro horario.");
        }

        delegarProximo(exame);
    }

    private boolean isMesmoExame(Exame existente, Exame atual) {
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
