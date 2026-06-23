package com.cine.loyalty_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgregarPuntosDTO {

    @NotNull(message = "Los puntos a agregar son obligatorios")
    @Min(value = 1, message = "Debe agregar al menos 1 punto")
    private Integer puntos;

}
