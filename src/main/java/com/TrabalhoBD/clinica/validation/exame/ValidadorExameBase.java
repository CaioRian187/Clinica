package com.TrabalhoBD.clinica.validation.exame;

import com.TrabalhoBD.clinica.models.Exame;

/**
 * GoF — Chain of Responsibility
 *
 * Base que elimina o boilerplate de delegação para os validadores de Exame.
 */
public abstract class ValidadorExameBase implements ValidadorExame {

    private ValidadorExame proximo;

    @Override
    public void setProximo(ValidadorExame proximo) {
        this.proximo = proximo;
    }

    protected void delegarProximo(Exame exame) {
        if (proximo != null) {
            proximo.validar(exame);
        }
    }
}
