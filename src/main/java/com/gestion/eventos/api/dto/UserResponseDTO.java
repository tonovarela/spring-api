package com.gestion.eventos.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private Long id;
    private String name;
    private String username;
    private String email;

    private List<RolDTO> roles;

    private List<EventSummaryDTO> eventSummaries;

}
