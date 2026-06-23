package com.cine.loyalty_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyDTO {

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long userId;

    @NotNull(message = "Los puntos son obligatorios")
    @Min(value = 0, message = "Los puntos no pueden ser negativos")
    private Integer cinepoints;

}
