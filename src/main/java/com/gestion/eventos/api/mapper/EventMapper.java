package com.gestion.eventos.api.mapper;

import com.gestion.eventos.api.domain.Event;
import com.gestion.eventos.api.dto.EventRequestDTO;
import com.gestion.eventos.api.dto.EventResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper( componentModel = "spring")
public interface EventMapper {

    Event toEvent(EventRequestDTO eventRequestDTO);

    EventResponseDTO toEventResponseDTO(Event event);

    List<EventResponseDTO> toEventResponseDTOList(List<Event> events);

    void updateEventFromDTO(EventRequestDTO eventRequestDTO,@MappingTarget Event event);
}
