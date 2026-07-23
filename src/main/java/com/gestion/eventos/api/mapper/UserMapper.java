package com.gestion.eventos.api.mapper;

import com.gestion.eventos.api.domain.Role;
import com.gestion.eventos.api.domain.User;
import com.gestion.eventos.api.exception.ResouceNotFoundException;
import com.gestion.eventos.api.security.dto.RegisterDTO;

import com.gestion.eventos.api.repository.IRoleRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;


@Mapper( componentModel = "spring")
public abstract class UserMapper {


    @Autowired
    protected IRoleRepository roleRepository;
    @Mapping(target = "password",ignore = true)
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRoleStringToRoles")
    public abstract  User toUser(RegisterDTO registerDTO);

    @Named("mapRoleStringToRoles")
    public Set<Role> mapRoleStringToRoles(Set<String> roles) {
        if (roles == null   || roles.isEmpty()) {
            return roleRepository.findByName("ROLE_USER")
                    .map(Collections::singleton)
                    .orElseThrow(() -> new ResouceNotFoundException("Error: Role is not found."));
        }
        return roles.stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new ResouceNotFoundException("Error: Role is not found.")))
                .collect(Collectors.toSet());
    }


}
