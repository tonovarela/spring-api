package com.gestion.eventos.api.service.implementation;

import com.gestion.eventos.api.domain.Category;
import com.gestion.eventos.api.domain.Event;
import com.gestion.eventos.api.domain.Speaker;
import com.gestion.eventos.api.dto.EventRequestDTO;
import com.gestion.eventos.api.dto.EventResponseDTO;
import com.gestion.eventos.api.exception.ResouceNotFoundException;
import com.gestion.eventos.api.mapper.EventMapper;
import com.gestion.eventos.api.repository.IEventRepository;
import com.gestion.eventos.api.service.interfaces.IEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService implements IEventService {

    private final IEventRepository eventRepository;
    private final EventMapper eventMapper;
    private final CategoryService categoryService;
    private final SpeakerService speakerService;


    @Override
    @Transactional(readOnly = true)
    public Page<EventResponseDTO> findAll(String name, Pageable pageable) {
        log.debug("findAll() - name={}, pageable={}", name, pageable);
        Page<Event> eventsPages;
        if (name != null && !name.trim().isEmpty()) {
            log.debug("findAll() - aplicando filtro por nombre: {}", name);
            eventsPages = eventRepository.findByNameContainingIgnoreCase(name, pageable);
        } else {
            log.debug("findAll() - sin filtro, consultando página completa");
            eventsPages = eventRepository.findAll(pageable);
        }
        List<EventResponseDTO> eventsList = eventsPages.getContent().stream()
                .map(eventMapper::toEventResponseDTO)
                .toList();
        log.debug("findAll() - eventos en la página: {}, total: {}", eventsList.size(), eventsPages.getTotalElements());
        return new PageImpl<>(eventsList, pageable, eventsPages.getTotalElements());
    }

    @Override
    @Transactional
    public Event save(EventRequestDTO requestDTO) {
        log.debug("save() - creando evento: {}", requestDTO);
        Event event = eventMapper.toEvent(requestDTO);
        Category category = categoryService.findById(requestDTO.getCategoryId());
        event.setCategory(category);
        if(requestDTO.getSpeakersIds() != null && !requestDTO.getSpeakersIds().isEmpty()) {
            Set<Speaker> speakers = requestDTO
                    .getSpeakersIds()
                    .stream()
                    .map(speakerService::findById)
                    .collect(Collectors.toSet());
            log.debug("save() - asociando {} speaker(s): {}", speakers.size(), requestDTO.getSpeakersIds());
            speakers.forEach(event::addSpeakers);
        }
        Event savedEvent = eventRepository.save(event);
        log.debug("save() - evento creado con id={}", savedEvent.getId());
        return savedEvent;

    }



    @Override
    public Event findById(Long id) {
        log.debug("findById() - buscando evento con id={}", id);
        return eventRepository.findById(id).orElseThrow(
                ()-> {
                    log.debug("findById() - evento no encontrado con id={}", id);
                    return new ResouceNotFoundException("Evento no encontrado con id " + id);
                }
        );
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.debug("deleteById() - eliminando evento id={}", id);
        Event event = this.findById(id);
        eventRepository.delete(event);
        log.debug("deleteById() - evento id={} eliminado", id);
    }

    @Override
    @Transactional
    public Event update(Long id, EventRequestDTO requestDTO) {
        log.debug("update() - actualizando evento id={} con: {}", id, requestDTO);
        Event event = eventRepository.findById(id).orElseThrow(
                () -> {
                    log.debug("update() - evento no encontrado con id={}", id);
                    return new ResouceNotFoundException("Evento no encontrado con id " + id);
                }
        );
        eventMapper.updateEventFromDTO(requestDTO, event);
        if(!event.getCategory().getId().equals(requestDTO.getCategoryId())) {
            log.debug("update() - cambiando categoría de {} a {}", event.getCategory().getId(), requestDTO.getCategoryId());
            Category category = categoryService.findById(requestDTO.getCategoryId());
            event.setCategory(category);
        }

        Set<Speaker> updatedSpeakers;
        if (requestDTO.getSpeakersIds() != null && !requestDTO.getSpeakersIds().isEmpty()) {
            updatedSpeakers = requestDTO.getSpeakersIds()
                                            .stream()
                                            .map(speakerService::findById)
                    .collect(Collectors.toSet());
        } else {
            updatedSpeakers = new HashSet<>();
        }

        log.debug("update() - speakers actuales: {}, speakers solicitados: {}",
                event.getSpeakers().size(), updatedSpeakers.size());
        new HashSet<>(event.getSpeakers())
                .forEach(currentSpeaker -> {
                    if (!updatedSpeakers.contains(currentSpeaker)) {
                        log.debug("update() - quitando speaker id={} del evento id={}", currentSpeaker.getId(), id);
                        event.removeSpeakers(currentSpeaker);
                    }
                });

        updatedSpeakers.forEach(newSpeaker ->
        {
            if (!event.getSpeakers().contains(newSpeaker)) {
                log.debug("update() - agregando speaker id={} al evento id={}", newSpeaker.getId(), id);
                event.addSpeakers(newSpeaker);
            }
        });

        Event updatedEvent = eventRepository.save(event);
        log.debug("update() - evento id={} actualizado", id);
        return updatedEvent;

    }


    @Transactional(readOnly = true)
    public List<Event> getAllEventsAndTheirDetailsProblematic(){
        log.debug("getAllEventsAndTheirDetailsProblematic() - carga LAZY, provoca N+1 queries");
        List<Event> events = eventRepository.findAll();
        events.forEach(event -> {
           var speakerNames = event.getSpeakers().stream().map(Speaker::getName).collect(Collectors.toSet());
           var categoryEvent = event.getCategory().getName();
           var attendedUsers = event.getAttendedUsers().size();
           log.debug("Evento={} | Categoría={} | Speakers={} | Asistentes={}",
                   event.getName(), categoryEvent, speakerNames, attendedUsers);
        });
        log.debug("getAllEventsAndTheirDetailsProblematic() - eventos recuperados: {}", events.size());
        return events;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> getAllEventsAndTheirDetailsOptimizedWithJoinFetch() {
        log.debug("getAllEventsAndTheirDetailsOptimizedWithJoinFetch() - carga con JOIN FETCH");
        List<Event> events = eventRepository.findAllWithCategoryAndSpeakers();
        events.forEach(event ->
            log.debug("Evento={} | Categoría={} | Speakers={}",
                    event.getName(),
                    event.getCategory().getName(),
                    event.getSpeakers().stream().map(Speaker::getName).collect(Collectors.joining(",")))
        );
        log.debug("getAllEventsAndTheirDetailsOptimizedWithJoinFetch() - eventos recuperados: {}", events.size());
        return events;
    }

    @Override
    public List<Event> getAllWithDetails() {
        log.debug("getAllWithDetails() - carga con EntityGraph");
        List<Event> events = eventRepository.findAllWithAllDetails();
        events.forEach(event ->
            log.debug("Evento={} | Categoría={} | Speakers={} | Asistentes={}",
                    event.getName(),
                    event.getCategory().getName(),
                    event.getSpeakers().stream().map(Speaker::getName).collect(Collectors.joining(",")),
                    event.getAttendedUsers().size())
        );
        log.debug("getAllWithDetails() - eventos recuperados: {}", events.size());
        return events;
    }
}
