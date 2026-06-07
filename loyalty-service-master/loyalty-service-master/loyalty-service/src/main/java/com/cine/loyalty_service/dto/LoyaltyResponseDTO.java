package com.cine.loyalty_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyResponseDTO {

    private Long id;
    private Long userId;
    private Integer cinepoints;

}
