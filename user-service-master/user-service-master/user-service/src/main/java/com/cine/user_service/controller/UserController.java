package com.cine.user_service.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cine.user_service.dto.UserDTO;
import com.cine.user_service.dto.UserResponseDTO;
import com.cine.user_service.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    // CREAR USUARIO
    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserDTO dto) {
        logger.info("POST /users - Solicitud para crear usuario: {}", dto.getCorreo());
        return ResponseEntity.ok(service.create(dto));
    }

    // OBTENER TODOS
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAll() {
        logger.info("GET /users - Solicitud para obtener todos los usuarios");
        return ResponseEntity.ok(service.getAll());
    }

    // OBTENER POR ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable Long id) {
        logger.info("GET /users/{} - Solicitud para obtener usuario por ID", id);
        return ResponseEntity.ok(service.getById(id));
    }

    // ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("DELETE /users/{} - Solicitud para eliminar usuario", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
