package com.gestion.eventos.api.repository;

import com.gestion.eventos.api.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface IRoleRepository extends JpaRepository<Role, Long> {

    public Optional<Role> findByName(String name);
}
