package com.TrabalhoBD.clinica.validation.exame;

import com.TrabalhoBD.clinica.models.Exame;

/**
 * GoF — Chain of Responsibility
 *
 * Contrato de cada elo da cadeia de validação de Exame.
 */
public interface ValidadorExame {

    void setProximo(ValidadorExame proximo);

    void validar(Exame exame);
}
