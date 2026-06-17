package com.TrabalhoBD.clinica.dtos;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ExameRequestDTO(

        @NotBlank(message = "O nome do exame é obrigatório.")
        String nome,

        @NotNull(message = "A data e horário do exame é obrigatório.")
        @FutureOrPresent(message = "Você não pode agendar um exame em uma data e horário que já passaram.")
        LocalDateTime dataHora,

        @NotBlank(message = "A descrição do exame é obrigatória.")
        String descricao,

        @NotNull(message = "O Id do médico é obrigatório.")
        Long medicoId,

        @NotNull(message = "O Id do paciente é obrigatório.")
        Long pacienteId

) {
}
