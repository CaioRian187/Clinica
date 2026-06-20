package com.TrabalhoBD.clinica.dtos;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReceitaRequestDTO(

        @NotNull(message = "A data de emissão da receita é obrigatório.")
        @FutureOrPresent(message = "Você não pode colocar uma data que já passou em uma receita.")
        LocalDateTime dataEmissao,

        @NotBlank(message = "O medicamento não pode ser nulo ou vazio.")
        String medicamento,

        @NotBlank(message = "A dosagem não pode ser nula ou vazia.")
        String dosagem,

        @NotBlank(message = "As instruções não podem ser nulas ou vazias.")
        String instrucoes,

        @NotNull(message = "O Id da consulta não pode ser nulo.")
        Long consultaId

) {
}
