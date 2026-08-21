package com.gestion.eventos.api.controller;


import com.gestion.eventos.api.domain.User;

import com.gestion.eventos.api.security.service.IUserService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
@Slf4j
public class UserController {

    private final IUserService userService;
    @GetMapping
    public List<User> findAll() {
        log.debug("GET /api/v1/users - obteniendo todos los usuarios");
        var users = userService.findAll();
        log.debug("Usuarios encontrados: {}", users.size());
        return users;
    }



}
