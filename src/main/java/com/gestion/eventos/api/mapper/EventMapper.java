package com.gestion.eventos.api.mapper;

import com.gestion.eventos.api.domain.Event;
import com.gestion.eventos.api.dto.EventRequestDTO;
import com.gestion.eventos.api.dto.EventResponseDTO;
import com.gestion.eventos.api.dto.EventSummaryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper( componentModel = "spring")
public interface EventMapper {

    @Mapping(target="id",ignore = true)
    @Mapping(target="category",ignore = true)
    @Mapping(target="speakers",ignore = true)
    @Mapping(target="attendedUsers",ignore = true)
    Event toEvent(EventRequestDTO eventRequestDTO);


     @Mapping(target="categoryId", source="category.id")
     @Mapping(target="categoryName", source="category.name")
     @Mapping(target="speakers", source="speakers")
    EventResponseDTO toEventResponseDTO(Event event);
    List<EventResponseDTO> toEventResponseDTOList(List<Event> events);






    @Mapping(target="id",ignore = true)
    @Mapping(target="category",ignore = true)
    @Mapping(target="speakers",ignore = true)
    @Mapping(target="attendedUsers",ignore = true)
    void updateEventFromDTO(EventRequestDTO eventRequestDTO,@MappingTarget Event event);



    EventSummaryDTO toEventSummaryDTO(Event event);
    List<EventSummaryDTO> toEventSummaryDTOList(List<Event> events);




}

