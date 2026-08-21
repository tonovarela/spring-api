package com.gestion.eventos.api.mapper;

import com.gestion.eventos.api.domain.Role;
import com.gestion.eventos.api.dto.RolDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RolMapper {
    RolDTO toDTO(Role rol);
    Role toEntity(RolDTO rolDTO);

    List<RolDTO> toDTOList(List<Role> roles);
}
