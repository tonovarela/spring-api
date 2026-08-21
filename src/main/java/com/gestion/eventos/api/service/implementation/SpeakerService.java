package com.gestion.eventos.api.service.implementation;

import com.gestion.eventos.api.domain.Speaker;
import com.gestion.eventos.api.dto.SpeakerRequestDTO;
import com.gestion.eventos.api.exception.ResouceNotFoundException;
import com.gestion.eventos.api.mapper.SpeakerMapper;
import com.gestion.eventos.api.repository.ISpeakerRepository;
import com.gestion.eventos.api.service.interfaces.ISpeakerService;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor

public class SpeakerService implements ISpeakerService {


    private final ISpeakerRepository speakerRepository;
    private  final SpeakerMapper speakerMapper;

    @Override
    @Transactional
    public Speaker save(SpeakerRequestDTO speakerRequestDTO) {
        Speaker speaker = speakerMapper.toEntity(speakerRequestDTO);
        return  speakerRepository.save(speaker);

    }

    @Override
    @Transactional(readOnly = true)
    public Speaker findById(Long id) {
        return speakerRepository.findById(id).orElseThrow(()->
                new ResouceNotFoundException("Speaker id"+id));
    }

    @Override
    @Transactional
    public Speaker update(Long id, SpeakerRequestDTO speakerRequestDTO) {
        Speaker existingSpeaker = speakerRepository.findById(id).orElseThrow(()->
                new ResouceNotFoundException("Speaker id"+id));
        speakerMapper.updateSpeakerFromDTO(speakerRequestDTO, existingSpeaker);
        return speakerRepository.save(existingSpeaker);
    }


    @Override
    @Transactional
    public void deleteById(Long id) {

        if (!speakerRepository.existsById(id)) {
            throw new ResouceNotFoundException("Speaker id"+id);
        }
        speakerRepository.deleteById(id);

    }

    @Override
    @Transactional(readOnly = true)
    public List<Speaker> findAll() {
        return speakerRepository.findAll();
    }
}
