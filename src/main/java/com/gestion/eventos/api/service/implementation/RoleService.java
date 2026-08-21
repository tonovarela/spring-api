package com.gestion.eventos.api.service.implementation;

import com.gestion.eventos.api.domain.Role;
import com.gestion.eventos.api.exception.ResouceNotFoundException;
import com.gestion.eventos.api.repository.IRoleRepository;
import com.gestion.eventos.api.service.interfaces.IRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService implements IRoleService {
    private final IRoleRepository roleRepository;
    @Override
    public List<Role> findAll() {
        log.debug("findAll() - consultando todos los roles");
        List<Role> roles = roleRepository.findAll();
        log.debug("findAll() - roles recuperados: {}", roles.size());
        return   roles;
    }

    @Override
    public Role save(Role role) {
        log.debug("save() - guardando rol: {}", role);
        Role savedRole = roleRepository.save(role);
        log.debug("save() - rol guardado con id={}", savedRole.getId());
        return savedRole;
    }

    @Override
    public Role findById(Long id) {
        log.debug("findById() - buscando rol con id={}", id);
        return roleRepository.findById(id).orElseThrow(
                ()-> {
                    log.debug("findById() - rol no encontrado con id={}", id);
                    return new ResouceNotFoundException("Rol no encontrado con id " + id);
                }
        );
    }

    @Override
    public void deleteById(Long id) {
        log.debug("deleteById() - eliminando rol id={}", id);
        roleRepository.deleteById(id);
        log.debug("deleteById() - rol id={} eliminado", id);
    }
}
