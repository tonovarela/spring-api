package com.gestion.eventos.api.repository;

import com.gestion.eventos.api.domain.Event;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IEventRepository extends JpaRepository<Event, Long> {

    Page<Event> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("Select e from Event e JOIN FETCH e.category LEFT JOIN FETCH e.speakers")
    List<Event> findAllWithCategoryAndSpeakers();

    
//    @Query("Select e from Event e JOIN FETCH e.category LEFT JOIN FETCH e.speakers where e.id = :id")
//    Optional<Event> findById(Long id);


    @Override
    @NotNull
    @EntityGraph(attributePaths = {"category", "speakers"})
    List<Event> findAll();




    @Override
    @NotNull
    @EntityGraph(attributePaths = {"category", "speakers"})
    Optional<Event> findById(Long id);

    @EntityGraph(attributePaths = {"category", "speakers","attendedUsers"})
    @Query("select e from Event e")
    List<Event> findAllWithAllDetails();


}