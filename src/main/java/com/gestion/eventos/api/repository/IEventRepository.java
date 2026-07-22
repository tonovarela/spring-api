package com.gestion.eventos.api.repository;

import com.gestion.eventos.api.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEventRepository extends JpaRepository<Event, Long> {
}