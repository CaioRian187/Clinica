package com.TrabalhoBD.clinica.validation.consulta;

import com.TrabalhoBD.clinica.models.Consulta;

/**
 * GoF — Chain of Responsibility
 *
 * Classe base que elimina o boilerplate de "guardar e chamar o próximo".
 * Cada validador concreto só precisa implementar executar(consulta)
 * e chamar super.delegarProximo(consulta) quando quiser continuar a cadeia.
 */
public abstract class ValidadorConsultaBase implements ValidadorConsulta {

    private ValidadorConsulta proximo;

    @Override
    public void setProximo(ValidadorConsulta proximo) {
        this.proximo = proximo;
    }

    /**
     * Propaga para o próximo elo, se existir.
     * Chamado pelos filhos ao final de executar().
     */
    protected void delegarProximo(Consulta consulta) {
        if (proximo != null) {
            proximo.validar(consulta);
        }
    }
}
