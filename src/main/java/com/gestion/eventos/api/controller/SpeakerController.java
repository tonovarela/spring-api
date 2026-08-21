package com.gestion.eventos.api.controller;

import com.gestion.eventos.api.dto.SpeakerRequestDTO;
import com.gestion.eventos.api.dto.SpeakerResponseDTO;
import com.gestion.eventos.api.mapper.SpeakerMapper;
import com.gestion.eventos.api.service.implementation.SpeakerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/speakers")
@RequiredArgsConstructor
@Slf4j
public class SpeakerController {

  private final SpeakerService speakerService;
  private final SpeakerMapper speakerMapper;

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
    public ResponseEntity<SpeakerResponseDTO> createSpeaker(@Valid @RequestBody SpeakerRequestDTO speakerRequestDTO) {

        log.debug("POST /api/v1/speakers - creando speaker: {}", speakerRequestDTO);
        var speaker = speakerService.save(speakerRequestDTO);
        var speakerResponseDTO = speakerMapper.toDTO(speaker);
        log.debug("Speaker creado: {}", speakerResponseDTO);
        return new ResponseEntity<>(speakerResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<SpeakerResponseDTO>> getAllSpeakers() {
        log.debug("GET /api/v1/speakers - obteniendo todos los speakers");
        var speakers = speakerService.findAll();
        var speakerResponseDTOs = speakerMapper.toResponseDtoList(speakers);
        log.debug("Speakers encontrados: {}", speakerResponseDTOs.size());
        return  new ResponseEntity<>(speakerResponseDTOs, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{id}")
    public ResponseEntity<SpeakerResponseDTO> getSpeakerById(@PathVariable  Long id) {
        log.debug("GET /api/v1/speakers/{} - buscando speaker", id);
        var speaker = speakerService.findById(id);
        var speakerResponseDTO = speakerMapper.toDTO(speaker);
        log.debug("Speaker encontrado: {}", speakerResponseDTO);
        return new ResponseEntity<>(speakerResponseDTO, HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSpeaker(@PathVariable Long id) {
        log.debug("DELETE /api/v1/speakers/{} - eliminando speaker", id);
        speakerService.deleteById(id);
        log.debug("Speaker {} eliminado", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SpeakerResponseDTO> updateSpeaker(@PathVariable Long id,
                                                            @Valid @RequestBody SpeakerRequestDTO speakerRequestDTO) {
        log.debug("PUT /api/v1/speakers/{} - actualizando speaker con: {}", id, speakerRequestDTO);
        var updatedSpeaker = speakerService.update(id, speakerRequestDTO);
        var speakerResponseDTO = speakerMapper.toDTO(updatedSpeaker);
        log.debug("Speaker {} actualizado", id);
        return new ResponseEntity<>(speakerResponseDTO, HttpStatus.OK);

    }












}
