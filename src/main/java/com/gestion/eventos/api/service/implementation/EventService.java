package com.gestion.eventos.api.service.implementation;

import com.gestion.eventos.api.domain.Event;
import com.gestion.eventos.api.exception.ResouceNotFoundException;
import com.gestion.eventos.api.repository.IEventRepository;
import com.gestion.eventos.api.service.interfaces.IEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService implements IEventService {

    private final IEventRepository eventRepository;

    @Override
    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    @Override
    public Event save(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Event findById(Long id) {
        return eventRepository.findById(id).orElseThrow(
                ()-> new ResouceNotFoundException("Evento no encontrado con id " + id)
        );
    }

    @Override
    public void deleteById(Long id) {
        Event event = this.findById(id);
        eventRepository.delete(event);
    }



}
