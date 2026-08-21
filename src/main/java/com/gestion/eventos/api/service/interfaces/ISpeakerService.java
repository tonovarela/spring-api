package com.gestion.eventos.api.service.interfaces;

import com.gestion.eventos.api.domain.Speaker;
import com.gestion.eventos.api.dto.SpeakerRequestDTO;

import java.util.List;

public interface ISpeakerService {


    Speaker save(SpeakerRequestDTO speakerRequestDTO);
    Speaker findById(Long id);
    Speaker update(Long id, SpeakerRequestDTO speakerRequestDTO);
    void deleteById(Long id);
    List<Speaker> findAll();

}
