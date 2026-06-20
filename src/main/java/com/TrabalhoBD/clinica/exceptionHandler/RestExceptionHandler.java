package com.TrabalhoBD.clinica.exceptionHandler;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorMessage> DataIntegrityViolationExceptionHandler(DataIntegrityViolationException exception){
        ErrorMessage response = new ErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception){
        ErrorMessage response = new ErrorMessage(HttpStatus.BAD_REQUEST, "Erro da validação, você deixou algum campo obrigatório vázio");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> responseStatusExceptionHandle(ResponseStatusException exception){
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("status: ", exception.getStatusCode());
        response.put("message: ", exception.getMessage());
        return ResponseEntity.status(exception.getStatusCode()).body(response);
    }
}
