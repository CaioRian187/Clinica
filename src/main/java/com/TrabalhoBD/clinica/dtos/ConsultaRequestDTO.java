package com.TrabalhoBD.clinica.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;


public record ConsultaRequestDTO(

        @NotNull(message = "A data e horário da consulta são obrigatórios.")
        @FutureOrPresent(message = "Você não pode agendar uma consulta em uma data e horário que já passaram.")
        LocalDateTime datahora,

        @NotBlank(message = "As observações não podem ser nulas ou vazias.")
        String observacoes,

        @NotNull(message = "O Id do médico não pode ser nulo.")
        Long medicoId,

        @NotNull(message = "O Id do paciente não pode ser nulo.")
        Long pacienteId

) {
}
