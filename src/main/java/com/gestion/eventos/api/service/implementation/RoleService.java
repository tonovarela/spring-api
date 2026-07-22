package com.gestion.eventos.api.service.implementation;

import com.gestion.eventos.api.domain.Role;
import com.gestion.eventos.api.exception.ResouceNotFoundException;
import com.gestion.eventos.api.repository.IRoleRepository;
import com.gestion.eventos.api.service.interfaces.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {
    private final IRoleRepository roleRepository;
    @Override
    public List<Role> findAll() {
        return   roleRepository.findAll();
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role findById(Long id) {
        return roleRepository.findById(id).orElseThrow(
                ()->  new ResouceNotFoundException("Rol no encontrado con id " + id)
        );
    }

    @Override
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }
}
