package com.cine.loyalty_service.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cine.loyalty_service.dto.AgregarPuntosDTO;
import com.cine.loyalty_service.dto.LoyaltyDTO;
import com.cine.loyalty_service.dto.LoyaltyResponseDTO;
import com.cine.loyalty_service.service.LoyaltyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/loyalty")
public class LoyaltyController {

    private static final Logger logger = LoggerFactory.getLogger(LoyaltyController.class);

    private final LoyaltyService service;

    public LoyaltyController(LoyaltyService service) {
        this.service = service;
    }

    // CREAR 
    @PostMapping
    public ResponseEntity<LoyaltyResponseDTO> create(@Valid @RequestBody LoyaltyDTO dto) {
        logger.info("POST /loyalty - Solicitud para crear cuenta de cinepoints para userId: {}", dto.getUserId());
        return ResponseEntity.ok(service.create(dto));
    }

    //OBTENER TODOS
    @GetMapping
    public ResponseEntity<List<LoyaltyResponseDTO>> getAll() {
        logger.info("GET /loyalty - Solicitud para obtener todas las cuentas");
        return ResponseEntity.ok(service.getAll());
    }

    //OBTENER POR USER ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<LoyaltyResponseDTO> getByUserId(@PathVariable Long userId) {
        logger.info("GET /loyalty/user/{} - Solicitud para obtener cuenta por userId", userId);
        return ResponseEntity.ok(service.getByUserId(userId));
    }

    //AGREGAR PUNTOS
    @PostMapping("/add/{userId}")
    public ResponseEntity<LoyaltyResponseDTO> addPoints(@PathVariable Long userId, @Valid @RequestBody AgregarPuntosDTO dto) {
        logger.info("PUT /loyalty/add/{} - Solicitud para agregar {} puntos", userId, dto.getPuntos());
        return ResponseEntity.ok(service.addPoints(userId, dto));
    }

}
