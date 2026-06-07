package com.cine.user_service.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
 
@ControllerAdvice
public class GlobalExceptionHandler {
 
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
 
    // ERROR DE VALIDACIÓN (@Valid falló), 400 Bad Request
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarValidacion(MethodArgumentNotValidException ex) {
        logger.warn("Error de validación: {}", ex.getMessage());
 
        Map<String, String> erroresCampos = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            erroresCampos.put(error.getField(), error.getDefaultMessage());
        }
 
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("timestamp", LocalDateTime.now().toString());
        respuesta.put("status", 400);
        respuesta.put("error", "Error de validación");
        respuesta.put("detalles", erroresCampos);
 
        return ResponseEntity.badRequest().body(respuesta);
    }
 
    // RECURSO NO ENCONTRADO, 404 Not Found
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> manejarRuntime(RuntimeException ex) {
        logger.error("Recurso no encontrado: {}", ex.getMessage());
 
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("timestamp", LocalDateTime.now().toString());
        respuesta.put("status", 404);
        respuesta.put("error", "Recurso no encontrado");
        respuesta.put("mensaje", ex.getMessage());
 
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
    }
 
    // ERROR GENÉRICO INESPERADO, 500 Internal Server Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarGeneral(Exception ex) {
        logger.error("Error inesperado: {}", ex.getMessage(), ex);
 
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("timestamp", LocalDateTime.now().toString());
        respuesta.put("status", 500);
        respuesta.put("error", "Error interno del servidor");
        respuesta.put("mensaje", "Ocurrió un error inesperado");
 
        return ResponseEntity.internalServerError().body(respuesta);
    }
}
