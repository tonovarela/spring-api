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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/events")
@AllArgsConstructor
@Slf4j
@Tag(name="Eventos",description = "Endpoints para la gestión de eventos")
public class EventController {

    private final IEventService eventService;
    private final EventMapper eventMapper;

    @GetMapping("/with-entitygraph")
    public ResponseEntity<List<EventResponseDTO>> getAllWithEntityGraph() {
        log.debug("GET /api/v1/events/with-entitygraph - obteniendo eventos con EntityGraph");
        var events = eventService.getAllWithDetails();
        var eventsResponseDTO = eventMapper.toEventResponseDTOList(events);
        log.debug("Eventos obtenidos con EntityGraph: {}", eventsResponseDTO.size());
        return ResponseEntity.ok(eventsResponseDTO);

    }

    @GetMapping("/problematic")
    public ResponseEntity<List<EventResponseDTO>> getProblematicEvents() {
        log.debug("GET /api/v1/events/problematic - obteniendo eventos (consulta con N+1)");
        var problematicEvents = eventService.getAllEventsAndTheirDetailsProblematic();
        var eventResponseDTOs = eventMapper.toEventResponseDTOList(problematicEvents);
        log.debug("Eventos obtenidos (problematic): {}", eventResponseDTOs.size());
        return ResponseEntity.ok(eventResponseDTOs);

    }

    @GetMapping("/optimized")
    public ResponseEntity<List<EventResponseDTO>> getOptimisticEvents() {
        log.debug("GET /api/v1/events/optimized - obteniendo eventos con JOIN FETCH");
        var events = eventService.getAllEventsAndTheirDetailsOptimizedWithJoinFetch();
        var eventResponseDTOs = eventMapper.toEventResponseDTOList(events);
        log.debug("Eventos obtenidos (optimized): {}", eventResponseDTOs.size());
        return ResponseEntity.ok(eventResponseDTOs);
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<Page<EventResponseDTO>> getEvents(
            @RequestParam(required = false) String name,
            @PageableDefault(page = 0, size = 10, sort = "name") Pageable pageable
    ) {
        log.debug("GET /api/v1/events - filtro name={}, pageable={}", name, pageable);
        var eventList = eventService.findAll(name,pageable);
        log.debug("Eventos encontrados: {} de un total de {}", eventList.getNumberOfElements(), eventList.getTotalElements());
        return ResponseEntity.ok(eventList);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventResponseDTO> createEvent(@Valid @RequestBody EventRequestDTO eventRequestDTO) {
        log.debug("POST /api/v1/events - creando evento: {}", eventRequestDTO);
        Event eventSaved = eventService.save(eventRequestDTO);
        EventResponseDTO responseDTO= eventMapper.toEventResponseDTO(eventSaved);
        log.debug("Evento creado con id={}", eventSaved.getId());
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
        log.debug("GET /api/v1/events/{} - buscando evento", id);
        Event event = eventService.findById(id);
        EventResponseDTO responseDTO = eventMapper.toEventResponseDTO(event);
        log.debug("Evento encontrado: {}", responseDTO);
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
        log.debug("PUT /api/v1/events/{} - actualizando evento con: {}", id, eventRequestDTO);
        Event updatedEvent = eventService.update(id, eventRequestDTO);
        EventResponseDTO responseDTO = eventMapper.toEventResponseDTO(updatedEvent);
        log.debug("Evento {} actualizado", id);
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
        log.debug("DELETE /api/v1/events/{} - eliminando evento", id);
        eventService.deleteById(id);
        log.debug("Evento {} eliminado", id);
        return ResponseEntity.noContent().build();
    }

}
