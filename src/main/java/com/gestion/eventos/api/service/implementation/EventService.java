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
public class EventService implements IEventService {

    private final IEventRepository eventRepository;
    private final EventMapper eventMapper;
    private final CategoryService categoryService;
    private final SpeakerService speakerService;


    @Override
    @Transactional(readOnly = true)
    public Page<EventResponseDTO> findAll(String name, Pageable pageable) {
        Page<Event> eventsPages;
        if (name != null && !name.trim().isEmpty()) {
            eventsPages = eventRepository.findByNameContainingIgnoreCase(name, pageable);
        } else {
            eventsPages = eventRepository.findAll(pageable);
        }
        List<EventResponseDTO> eventsList = eventsPages.getContent().stream()
                .map(eventMapper::toEventResponseDTO)
                .toList();
        return new PageImpl<>(eventsList, pageable, eventsPages.getTotalElements());
    }

    @Override
    @Transactional
    public Event save(EventRequestDTO requestDTO) {
        Event event = eventMapper.toEvent(requestDTO);
        Category category = categoryService.findById(requestDTO.getCategoryId());
        event.setCategory(category);
        if(requestDTO.getSpeakersIds() != null && !requestDTO.getSpeakersIds().isEmpty()) {
            Set<Speaker> speakers = requestDTO
                    .getSpeakersIds()
                    .stream()
                    .map(speakerService::findById)
                    .collect(Collectors.toSet());
            speakers.forEach(event::addSpeakers);
        }
        return eventRepository.save(event);

    }



    @Override
    public Event findById(Long id) {
        return eventRepository.findById(id).orElseThrow(
                ()-> new ResouceNotFoundException("Evento no encontrado con id " + id)
        );
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Event event = this.findById(id);
        eventRepository.delete(event);
    }

    @Override
    @Transactional
    public Event update(Long id, EventRequestDTO requestDTO) {
        Event event = eventRepository.findById(id).orElseThrow(
                () -> new ResouceNotFoundException("Evento no encontrado con id " + id)
        );
        eventMapper.updateEventFromDTO(requestDTO, event);
        if(!event.getCategory().getId().equals(requestDTO.getCategoryId())) {
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

        new HashSet<>(event.getSpeakers())
                .forEach(currentSpeaker -> {
                    if (!updatedSpeakers.contains(currentSpeaker)) {
                        event.removeSpeakers(currentSpeaker);
                    }
                });

        updatedSpeakers.forEach(newSpeaker ->
        {
            if (!event.getSpeakers().contains(newSpeaker))
                event.addSpeakers(newSpeaker);
        });

        return eventRepository.save(event);

    }


    @Transactional(readOnly = true)
    public List<Event> getAllEventsAndTheirDetailsProblematic(){
        List<Event> events = eventRepository.findAll();
        events.forEach(event -> {
           var sizeEvents =  event.getSpeakers().size();
           var setEvents = event.getSpeakers().stream().map(Speaker::getName).collect(Collectors.toSet());
           var categoryEvent = event.getCategory().getName();
           var attendedUsers= event.getAttendedUsers().size();
        });
        return events;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> getAllEventsAndTheirDetailsOptimizedWithJoinFetch() {
        List<Event> events = eventRepository.findAllWithCategoryAndSpeakers();
        events.forEach(event -> {
            System.out.println("Event"+event.getName()+
                            " Category: "+event.getCategory().getName()+
                            " Speakers: "+event.getSpeakers().stream().map(Speaker::getName).collect(Collectors.joining(",")));
        });
        return events;
    }

    @Override
    public List<Event> getAllWithDetails() {
        List<Event> events = eventRepository.findAllWithAllDetails();
        events.forEach(event -> {
            System.out.println("Event"+event.getName()+
                    " Category: "+event.getCategory().getName()+
                    " Speakers: "+event.getSpeakers().stream().map(Speaker::getName).collect(Collectors.joining(","))+
                    " Attended Users: "+event.getAttendedUsers().size());
        });
        return events;
    }
}
