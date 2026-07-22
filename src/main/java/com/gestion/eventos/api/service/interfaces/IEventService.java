package com.gestion.eventos.api.service.interfaces;

import com.gestion.eventos.api.domain.Event;

import java.util.List;

public interface IEventService {
    List<Event> findAll();
    Event save(Event event);
    Event findById(Long id);
    void deleteById(Long id);
}
