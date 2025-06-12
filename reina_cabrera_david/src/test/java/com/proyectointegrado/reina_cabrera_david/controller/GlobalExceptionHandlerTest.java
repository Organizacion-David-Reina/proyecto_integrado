package com.proyectointegrado.reina_cabrera_david.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.proyectointegrado.reina_cabrera_david.exceptions.InternalServerException;

public class GlobalExceptionHandlerTest {

    @Test
    void testHandleInternalServerError() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        String errorMessage = "Error interno";
        InternalServerException ex = new InternalServerException(errorMessage);

        ResponseEntity<Map<String, String>> response = handler.handleInternalServerError(ex);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("message"));
        assertEquals(errorMessage, response.getBody().get("message"));
    }
}
