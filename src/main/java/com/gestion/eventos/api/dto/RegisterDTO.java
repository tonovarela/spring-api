package com.gestion.eventos.api.dto;

import lombok.Data;


@Data
public class RegisterDTO {

    private String username;
    private String password;
    private String email;
    private String name;
}
