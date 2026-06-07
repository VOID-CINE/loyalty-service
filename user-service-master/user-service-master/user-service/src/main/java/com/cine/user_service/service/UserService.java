package com.cine.user_service.service;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cine.user_service.dto.UserDTO;
import com.cine.user_service.dto.UserResponseDTO;
import com.cine.user_service.model.User;
import com.cine.user_service.repository.UserRepository;

@Service

public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //MAPEAR entidad User a UserResponseDTO (Sin exponer password()
    private UserResponseDTO mapearRespuesta(User user) {
        return new UserResponseDTO(
            user.getId(),
            user.getNombre(),
            user.getCorreo(),
            user.getRol()
        );
    }

    // CREAR USUARIO
    public UserResponseDTO create(UserDTO dto) {
        logger.info("Creando usuario con correo: {}", dto.getCorreo());

        User user = new User();
        user.setNombre(dto.getNombre());
        user.setCorreo(dto.getCorreo());
        user.setPassword(dto.getPassword());
        user.setRol(dto.getRol());

        User guardado = userRepository.save(user);
        logger.info("Usuario creado con ID: {}", guardado.getId());

        return mapearRespuesta(guardado);
    }

    //OBTENER USUARIOS
    public List<UserResponseDTO> getAll() {
        logger.info("Obteniendo lista de todos los usuarios");
        
        return userRepository.findAll()
                .stream()
                .map(this::mapearRespuesta)
                .collect(Collectors.toList());
    }

    //OBTENER POR ID
    public UserResponseDTO getById(Long id) {
        logger.info("Buscando usuario con ID: {}", id);

        User user = userRepository.findById(id).orElseThrow(() -> {
            logger.error("Usuario no encontrado con ID: {}", id);
            return new RuntimeException("Usuario no encontrado con ID:");
        });

        return mapearRespuesta(user);
    }

    // ELIMINAR
    public void delete(Long id) {
        logger.info("Eliminando usuario con ID: {}", id);

        if (!userRepository.existsById(id)) {
            logger.error("No se encontró usuario para eliminar con ID: {}", id);
            throw new RuntimeException("Usuario no encontrado con ID:");
        }

        userRepository.deleteById(id);
        logger.info("Usuario con ID: {} eliminado correctamente", id);
    }


}
