package com.gestion.eventos.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Schema(description = "Dto para la creación de un evento")
public class EventRequestDTO {
   @Schema(description = "Nombre del evento", example = "Conferencia de Tecnología")
    @NotBlank(message="El nombre es requerido")
    private String name;
    @NotNull(message = "La fecha es requerida")
    private LocalDate date;
    @NotBlank(message = "El local es requerido")
    private String local;


    @NotNull(message = "La categoría es requerida")
    private Long categoryId;


    private Set<Long> speakersIds = new HashSet<>();

}
