package com.gestion.eventos.api.security.dto;

import lombok.Data;

@Data
public class JwtAuthResponseDTO {

    private String accessToken;
    private String tokenType= "Bearer";
}
