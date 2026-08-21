package com.gestion.eventos.api.controller;

import com.gestion.eventos.api.domain.Event;
import com.gestion.eventos.api.dto.EventRequestDTO;
import com.gestion.eventos.api.dto.EventResponseDTO;
import com.gestion.eventos.api.mapper.EventMapper;
import com.gestion.eventos.api.service.interfaces.IEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/events")
@AllArgsConstructor
@Tag(name="Eventos",description = "Endpoints para la gestión de eventos")
public class EventController {

    private final IEventService eventService;

    private final EventMapper eventMapper;

    @GetMapping("/with-entitygraph")
    public ResponseEntity<List<EventResponseDTO>> getAllWithEntityGraph() {
        var events = eventService.getAllWithDetails();
        var eventsResponseDTO = eventMapper.toEventResponseDTOList(events);
        return ResponseEntity.ok(eventsResponseDTO);

    }

    @GetMapping("/problematic")
    public ResponseEntity<List<EventResponseDTO>> getProblematicEvents() {
        var problematicEvents = eventService.getAllEventsAndTheirDetailsProblematic();
        var eventResponseDTOs = eventMapper.toEventResponseDTOList(problematicEvents);
        return ResponseEntity.ok(eventResponseDTOs);

    }

    @GetMapping("/optimized")
    public ResponseEntity<List<EventResponseDTO>> getOptimisticEvents() {
        var events = eventService.getAllEventsAndTheirDetailsOptimizedWithJoinFetch();
        var eventResponseDTOs = eventMapper.toEventResponseDTOList(events);
        return ResponseEntity.ok(eventResponseDTOs);
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<Page<EventResponseDTO>> getEvents(
            @RequestParam(required = false) String name,
            @PageableDefault(page = 0, size = 10, sort = "name") Pageable pageable
    ) {
        var eventList = eventService.findAll(name,pageable);
        return ResponseEntity.ok(eventList);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventResponseDTO> createEvent(@Valid @RequestBody EventRequestDTO eventRequestDTO) {

        Event eventSaved = eventService.save(eventRequestDTO);
        EventResponseDTO responseDTO= eventMapper.toEventResponseDTO(eventSaved);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Operation(summary = "Obtener un evento por su ID", description = "Devuelve los detalles de un evento específico según su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento encontrado y devuelto correctamente."),
            @ApiResponse(responseCode = "404", description = "Evento no encontrado con el ID proporcionado."),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. El usuario no tiene permisos para acceder a este recurso.")
    })
    public ResponseEntity<EventResponseDTO> getEvent(@PathVariable Long id) {
        Event event = eventService.findById(id);
        EventResponseDTO responseDTO = eventMapper.toEventResponseDTO(event);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento actualizado correctamente."),
            @ApiResponse(responseCode = "404", description = "Evento no encontrado con el ID proporcionado."),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. El usuario no tiene permisos para acceder a este recurso.")
    })
    public ResponseEntity<EventResponseDTO>  updateEvent(@PathVariable Long id, @Valid @RequestBody EventRequestDTO eventRequestDTO) {
        Event updatedEvent = eventService.update(id, eventRequestDTO);
        EventResponseDTO responseDTO = eventMapper.toEventResponseDTO(updatedEvent);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Evento eliminado correctamente."),
            @ApiResponse(responseCode = "404", description = "Evento no encontrado con el ID proporcionado."),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. El usuario no tiene permisos para acceder a este recurso.")
    })
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteById(id);
        return ResponseEntity.noContent().build();
    }







}
