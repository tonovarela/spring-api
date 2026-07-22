package com.gestion.eventos.api.dto;

import lombok.Data;

@Data
public class JwtAuthResponseDTO {

    private String accessToken;
    private String tokenType= "Bearer";
}
