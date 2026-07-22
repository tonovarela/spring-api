package com.gestion.eventos.api.controller;


import com.gestion.eventos.api.domain.User;

import com.gestion.eventos.api.service.interfaces.IUserService;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class UserController {

    private final IUserService userService;
    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }



}
