package com.gestion.eventos.api.dto;

import lombok.Data;

@Data
public class UserResponseDTO {

    private Long id;
    private String name;
    private String username;
    private String email;
    private String password;
}
