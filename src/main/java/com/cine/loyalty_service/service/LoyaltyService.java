package com.cine.loyalty_service.service;


import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.cine.loyalty_service.dto.AgregarPuntosDTO;
import com.cine.loyalty_service.dto.LoyaltyDTO;
import com.cine.loyalty_service.dto.LoyaltyResponseDTO;
import com.cine.loyalty_service.model.Loyalty;
import com.cine.loyalty_service.repository.LoyaltyRepository;

@Service
public class LoyaltyService {

    private static final Logger logger = LoggerFactory.getLogger(LoyaltyService.class);

    private final LoyaltyRepository repository;
    private final WebClient webClient;

    public LoyaltyService(LoyaltyRepository repository, WebClient webClient) {
        this.repository = repository;
        this.webClient = webClient;
    }

    // MAPEAR entidad Loyalty a LoyaltyResponseDTO
    private LoyaltyResponseDTO mapearRespuesta(Loyalty loyalty) {
        return new LoyaltyResponseDTO(
                loyalty.getId(),
                loyalty.getUserId(),
                loyalty.getCinepoints()
        );
    }

    // VALIDAR QUE EL USUARIO EXISTE EN USER-SERVICE
    private void validarUsuario(Long userId) {
        logger.info("Validando existencia del usuario con ID: {} en user-service", userId);
        try {
            webClient.get()
                    .uri("/users/{id}", userId)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
                logger.info("Usuario con ID: {} validado correctamente", userId);
        } catch (WebClientResponseException.NotFound e) {
            logger.error("Usuario con ID: {} no existe en user-service", userId);
            throw new RuntimeException("El usuario con ID " + userId + " no existe");
        }catch (Exception e) {
            logger.error("Error al conectar con user-service: {}", e.getMessage());
            throw new RuntimeException("No se pudo validar el usuario. El user-service no está disponible");
        }
    }

    // CREAR CUENTA
    public LoyaltyResponseDTO create(LoyaltyDTO dto) {
        logger.info("Creando cuenta de cinepoints para userId: {}", dto.getUserId());

        validarUsuario(dto.getUserId());

        //IllegalArgumentException, cae en el manejador 409 conflict
        if (repository.findByUserId(dto.getUserId()).isPresent()) {
            logger.warn("Ya existe una cuenta de cinepoints para userId: {}", dto.getUserId());
            throw new IllegalArgumentException("Ya existe una cuenta de cinepoints para el usuario ID: " + dto.getUserId());
        }

        Loyalty loyalty = new Loyalty();
        loyalty.setUserId(dto.getUserId());
        loyalty.setCinepoints(dto.getCinepoints());

        Loyalty guardado = repository.save(loyalty);
        logger.info("Cuenta creada exitosamente para userId: {} con {} cinepoints", dto.getUserId(), dto.getCinepoints());
        return mapearRespuesta(guardado);
    }

    // OBTENER TODOS
    public List<LoyaltyResponseDTO> getAll() {
        logger.info("Obteniendo todas las cuentas de cinepoints");
        return repository.findAll()
                .stream()
                .map(this::mapearRespuesta)
                .collect(Collectors.toList());
    }

    // OBTENER POR ID
    public LoyaltyResponseDTO getByUserId(Long userId) {
        logger.info("Buscando cuenta de cinepoints para userId: {}", userId);

        Loyalty loyalty = repository.findByUserId(userId).orElseThrow(() -> {
            logger.error("No se encontró cuenta de cinepoints para userId: {}", userId);
            return new RuntimeException("No se encontró una cuenta de cinepoints para el usuario ID: " + userId);
        });
        return mapearRespuesta(loyalty);
    }

    // AGREGAR PUNTOS
    public LoyaltyResponseDTO addPoints(Long userId, AgregarPuntosDTO dto) {
        logger.info("Agregando {} cinepints al userId: {}", dto.getPuntos(), userId);

        Loyalty loyalty = repository.findByUserId(userId).orElseThrow(() -> {
            logger.error("No se encontró cuenta para userId: {} al intentar agregar puntos" + userId);
            return new RuntimeException("No existe cuenta de cinepoints para el usuario ID: " + userId);
        });

        loyalty.setCinepoints(loyalty.getCinepoints() + dto.getPuntos());
        Loyalty actualizado = repository.save(loyalty);

        logger.info("Cinepoints actualizados para userId: {}. Nuevo saldo: {}", userId, actualizado.getCinepoints());
        return mapearRespuesta(actualizado);
    }
    
        
}

