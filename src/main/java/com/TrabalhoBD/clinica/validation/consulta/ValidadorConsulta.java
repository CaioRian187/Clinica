package com.TrabalhoBD.clinica.validation.consulta;

import com.TrabalhoBD.clinica.models.Consulta;

/**
 * GoF — Chain of Responsibility
 *
 * Contrato de cada elo da cadeia de validação de Consulta.
 * Cada implementação:
 *   1. Verifica sua própria regra de negócio.
 *   2. Se aprovada, delega para o próximo elo via proximo.validar(consulta).
 *   3. Se reprovada, lança ValidacaoAgendamentoException.
 */
public interface ValidadorConsulta {

    /**
     * Define qual é o próximo elo da cadeia.
     */
    void setProximo(ValidadorConsulta proximo);

    /**
     * Executa a validação e, se ok, propaga para o próximo elo.
     */
    void validar(Consulta consulta);
}
