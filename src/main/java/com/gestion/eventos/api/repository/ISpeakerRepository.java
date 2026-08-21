package com.gestion.eventos.api.repository;

import com.gestion.eventos.api.domain.Speaker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ISpeakerRepository extends JpaRepository<Speaker, Long> {


    Optional<Speaker> findByEmail(String email);
    boolean existsByEmail(String email);

}
