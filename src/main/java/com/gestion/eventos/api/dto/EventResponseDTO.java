package com.gestion.eventos.api.dto;



import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Data
public class EventResponseDTO {

    private Long id;
    private String name;
    private LocalDate date;
    private String local;


    private Long categoryId;
    private String categoryName;

    private Set<SpeakerResponseDTO> speakers;
}
