package com.gestion.eventos.api.service.interfaces;

import com.gestion.eventos.api.domain.Role;

import java.util.List;

public interface IRoleService {
    List<Role> findAll();
    Role save(Role role);
    Role findById(Long id);
    void deleteById(Long id);

}
