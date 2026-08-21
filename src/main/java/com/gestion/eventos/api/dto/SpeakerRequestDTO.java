package com.gestion.eventos.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpeakerRequestDTO {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name should not exceed 100 characters")
    private String name;
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email should not exceed 100 characters")
    private String email;
    @Size(max = 500, message = "Bio should not exceed 500 characters")
    private String bio;
}
