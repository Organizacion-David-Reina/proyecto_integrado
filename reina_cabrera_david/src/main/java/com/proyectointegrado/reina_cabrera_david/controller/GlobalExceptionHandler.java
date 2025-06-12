package com.proyectointegrado.reina_cabrera_david.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.proyectointegrado.reina_cabrera_david.exceptions.InternalServerException;

/**
 * The Class GlobalExceptionHandler.
 * Handles exceptions globally across all controllers.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles InternalServerException exceptions.
     *
     * @param ex the InternalServerException thrown
     * @return ResponseEntity with a map containing the error message and HTTP BAD_REQUEST status
     */
    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<Map<String, String>> handleInternalServerError(InternalServerException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
