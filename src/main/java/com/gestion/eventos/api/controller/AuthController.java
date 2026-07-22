package com.gestion.eventos.api.controller;

import com.gestion.eventos.api.domain.Role;
import com.gestion.eventos.api.domain.User;
import com.gestion.eventos.api.dto.JwtAuthResponseDTO;
import com.gestion.eventos.api.dto.RegisterDTO;
import com.gestion.eventos.api.mapper.UserMapper;
import com.gestion.eventos.api.repository.IRoleRepository;
import com.gestion.eventos.api.repository.IUserRepository;
import com.gestion.eventos.api.security.jwt.JwtGenerator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

@RequestMapping("api/v1/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtGenerator jwtGenerator;
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponseDTO> authenticateUser(Authentication authentication) {

        String token = jwtGenerator.generateToken(authentication);
        JwtAuthResponseDTO jwtAuthResponseDTO = new JwtAuthResponseDTO();
        jwtAuthResponseDTO.setAccessToken(token);
        return new ResponseEntity<>(jwtAuthResponseDTO, HttpStatus.OK);
    }
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody  RegisterDTO registerDTO) {
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            return new ResponseEntity<>("Email is already in use!", HttpStatus.BAD_REQUEST);
        }

        User newUser = userMapper.toUser(registerDTO);
        newUser.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        System.out.println(newUser.getUsername() + ", Email: " + newUser.getEmail());
        System.out.println("Antes de guardarlo");

         Role rol = roleRepository.findByName("ROLE_USER").orElseThrow(
                 ()->
                         new RuntimeException("Error: Role is not found."));
         newUser.setRoles(Collections.singleton(rol));
         userRepository.save(newUser);
        System.out.println("Usuario registrado: " + newUser.getUsername() + ", Email: " + newUser.getEmail());
        return new ResponseEntity<>("Usuario registrado", HttpStatus.OK);

    }




}
