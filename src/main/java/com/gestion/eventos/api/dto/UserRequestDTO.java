package com.gestion.eventos.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRequestDTO {
    @NotBlank(message="El nombre es requerido")
    private String name;
    @NotBlank(message="El username es requerido")
    private String username;
    @NotBlank(message="El email es requerido")
    @Email(message = "El email no es válido")
    private String email;
    @NotBlank(message="El password es requerido")
    private String password;

    private Long id_role;


}
