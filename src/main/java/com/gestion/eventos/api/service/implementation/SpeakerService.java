package com.gestion.eventos.api.service.implementation;

import com.gestion.eventos.api.domain.Speaker;
import com.gestion.eventos.api.dto.SpeakerRequestDTO;
import com.gestion.eventos.api.exception.ResouceNotFoundException;
import com.gestion.eventos.api.mapper.SpeakerMapper;
import com.gestion.eventos.api.repository.ISpeakerRepository;
import com.gestion.eventos.api.service.interfaces.ISpeakerService;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpeakerService implements ISpeakerService {


    private final ISpeakerRepository speakerRepository;
    private  final SpeakerMapper speakerMapper;

    @Override
    @Transactional
    public Speaker save(SpeakerRequestDTO speakerRequestDTO) {
        log.debug("save() - creando speaker: {}", speakerRequestDTO);
        Speaker speaker = speakerMapper.toEntity(speakerRequestDTO);
        Speaker savedSpeaker = speakerRepository.save(speaker);
        log.debug("save() - speaker creado con id={}", savedSpeaker.getId());
        return  savedSpeaker;

    }

    @Override
    @Transactional(readOnly = true)
    public Speaker findById(Long id) {
        log.debug("findById() - buscando speaker con id={}", id);
        return speakerRepository.findById(id).orElseThrow(()-> {
            log.debug("findById() - speaker no encontrado con id={}", id);
            return new ResouceNotFoundException("Speaker id"+id);
        });
    }

    @Override
    @Transactional
    public Speaker update(Long id, SpeakerRequestDTO speakerRequestDTO) {
        log.debug("update() - actualizando speaker id={} con: {}", id, speakerRequestDTO);
        Speaker existingSpeaker = speakerRepository.findById(id).orElseThrow(()-> {
            log.debug("update() - speaker no encontrado con id={}", id);
            return new ResouceNotFoundException("Speaker id"+id);
        });
        speakerMapper.updateSpeakerFromDTO(speakerRequestDTO, existingSpeaker);
        Speaker updatedSpeaker = speakerRepository.save(existingSpeaker);
        log.debug("update() - speaker id={} actualizado", id);
        return updatedSpeaker;
    }


    @Override
    @Transactional
    public void deleteById(Long id) {

        log.debug("deleteById() - eliminando speaker id={}", id);
        if (!speakerRepository.existsById(id)) {
            log.debug("deleteById() - speaker no encontrado con id={}", id);
            throw new ResouceNotFoundException("Speaker id"+id);
        }
        speakerRepository.deleteById(id);
        log.debug("deleteById() - speaker id={} eliminado", id);

    }

    @Override
    @Transactional(readOnly = true)
    public List<Speaker> findAll() {
        log.debug("findAll() - consultando todos los speakers");
        List<Speaker> speakers = speakerRepository.findAll();
        log.debug("findAll() - speakers recuperados: {}", speakers.size());
        return speakers;
    }
}
