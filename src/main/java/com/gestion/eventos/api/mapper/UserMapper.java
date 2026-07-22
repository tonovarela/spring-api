package com.gestion.eventos.api.mapper;

import com.gestion.eventos.api.domain.User;
import com.gestion.eventos.api.dto.RegisterDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper( componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password",ignore = true)
    @Mapping(target = "roles",ignore = true)
    User toUser(RegisterDTO registerDTO);
}
