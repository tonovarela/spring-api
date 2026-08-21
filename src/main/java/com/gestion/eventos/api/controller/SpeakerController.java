package com.gestion.eventos.api.controller;

import com.gestion.eventos.api.dto.SpeakerRequestDTO;
import com.gestion.eventos.api.dto.SpeakerResponseDTO;
import com.gestion.eventos.api.mapper.SpeakerMapper;
import com.gestion.eventos.api.service.implementation.SpeakerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/speakers")
@RequiredArgsConstructor
public class SpeakerController {

  private final SpeakerService speakerService;
  private final SpeakerMapper speakerMapper;

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
    public ResponseEntity<SpeakerResponseDTO> createSpeaker(@Valid @RequestBody SpeakerRequestDTO speakerRequestDTO) {

        var speaker = speakerService.save(speakerRequestDTO);
        var speakerResponseDTO = speakerMapper.toDTO(speaker);
        return new ResponseEntity<>(speakerResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<SpeakerResponseDTO>> getAllSpeakers() {
        var speakers = speakerService.findAll();
        var speakerResponseDTOs = speakerMapper.toResponseDtoList(speakers);
        return  new ResponseEntity<>(speakerResponseDTOs, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{id}")
    public ResponseEntity<SpeakerResponseDTO> getSpeakerById(@PathVariable  Long id) {
        var speaker = speakerService.findById(id);
        var speakerResponseDTO = speakerMapper.toDTO(speaker);
        return new ResponseEntity<>(speakerResponseDTO, HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSpeaker(@PathVariable Long id) {
        speakerService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SpeakerResponseDTO> updateSpeaker(@PathVariable Long id,
                                                            @Valid @RequestBody SpeakerRequestDTO speakerRequestDTO) {
        var updatedSpeaker = speakerService.update(id, speakerRequestDTO);
        var speakerResponseDTO = speakerMapper.toDTO(updatedSpeaker);
        return new ResponseEntity<>(speakerResponseDTO, HttpStatus.OK);

    }












}
