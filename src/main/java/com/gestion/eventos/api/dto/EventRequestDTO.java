package com.gestion.eventos.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EventRequestDTO {

    @NotBlank(message="El nombre es requerido")
    private String name;
    @NotNull(message = "La fecha es requerida")
    private LocalDate date;
    @NotBlank(message = "El local es requerido")
    private String local;

}
