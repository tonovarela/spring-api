package com.gestion.eventos.api.controller;

import com.gestion.eventos.api.domain.Event;
import com.gestion.eventos.api.dto.EventRequestDTO;
import com.gestion.eventos.api.dto.EventResponseDTO;
import com.gestion.eventos.api.mapper.EventMapper;
import com.gestion.eventos.api.service.interfaces.IEventService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/events")
@AllArgsConstructor
public class EventController {

    private final IEventService eventService;

    private final EventMapper eventMapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<EventResponseDTO>> getEvents() {
        List<Event> events = eventService.findAll();
        List<EventResponseDTO> responseDTOs = eventMapper.toEventResponseDTOList(events);
        return ResponseEntity.ok(responseDTOs);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventResponseDTO> createEvent(@Valid @RequestBody EventRequestDTO eventRequestDTO) {
        Event eventToSave = eventMapper.toEvent(eventRequestDTO);
        Event eventSaved = eventService.save(eventToSave);
        EventResponseDTO responseDTO= eventMapper.toEventResponseDTO(eventSaved);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<EventResponseDTO> getEvent(@PathVariable Long id) {
        Event event = eventService.findById(id);
        EventResponseDTO responseDTO = eventMapper.toEventResponseDTO(event);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventResponseDTO>  updateEvent(@PathVariable Long id, @Valid @RequestBody EventRequestDTO eventRequestDTO) {
        Event event = eventService.findById(id);
        eventMapper.updateEventFromDTO(eventRequestDTO, event);
        Event updatedEvent = eventService.save(event);
        EventResponseDTO responseDTO = eventMapper.toEventResponseDTO(updatedEvent);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteById(id);
        return ResponseEntity.noContent().build();
    }







}
