package com.TrabalhoBD.clinica.exceptionHandler;

import com.TrabalhoBD.clinica.exceptionHandler.exception.ValidacaoAgendamentoException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorMessage> DataIntegrityViolationExceptionHandler(
            DataIntegrityViolationException exception) {
        ErrorMessage response = new ErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> MethodArgumentNotValidExceptionHandler(
            MethodArgumentNotValidException exception) {
        ErrorMessage response = new ErrorMessage(HttpStatus.BAD_REQUEST, exception.getFieldError().getDefaultMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // GoF — Chain of Responsibility: trata falhas de qualquer elo da cadeia de
    // validação
    @ExceptionHandler(ValidacaoAgendamentoException.class)
    public ResponseEntity<ErrorMessage> validacaoAgendamentoHandler(ValidacaoAgendamentoException exception) {
        ErrorMessage response = new ErrorMessage(HttpStatus.BAD_GATEWAY, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorMessage> responseStatusExceptionHandle(ResponseStatusException exception) {
        ErrorMessage response = new ErrorMessage((HttpStatus) exception.getStatusCode(), exception.getReason());
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
