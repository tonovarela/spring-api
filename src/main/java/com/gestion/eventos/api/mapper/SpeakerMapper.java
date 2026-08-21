package com.gestion.eventos.api.mapper;

import com.gestion.eventos.api.domain.Speaker;
import com.gestion.eventos.api.dto.SpeakerRequestDTO;
import com.gestion.eventos.api.dto.SpeakerResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SpeakerMapper {


    SpeakerResponseDTO toDTO(Speaker speaker);



    @Mapping(target = "events", ignore = true)
    @Mapping(target = "id", ignore = true)
    Speaker toEntity(SpeakerRequestDTO speakerDTO);


    List<SpeakerResponseDTO> toResponseDtoList(List<Speaker> speakers);

    @Mapping(target = "events", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateSpeakerFromDTO(SpeakerRequestDTO speakerDTO, @MappingTarget Speaker speaker);


}
