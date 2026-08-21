package com.gestion.eventos.api.security.controllers;

import com.gestion.eventos.api.domain.User;
import com.gestion.eventos.api.security.dto.JwtAuthResponseDTO;
import com.gestion.eventos.api.security.dto.LoginDTO;
import com.gestion.eventos.api.security.dto.RegisterDTO;
import com.gestion.eventos.api.mapper.UserMapper;
import com.gestion.eventos.api.repository.IUserRepository;
import com.gestion.eventos.api.security.jwt.JwtGenerator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1/auth")
@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final JwtGenerator jwtGenerator;
    private final IUserRepository userRepository;
    //private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponseDTO> authenticateUser(@RequestBody LoginDTO loginDTO) {

        log.debug("POST /api/v1/auth/login - intento de autenticación para username={}", loginDTO.getUsername());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
        );

        log.debug("Autenticación correcta para username={}, generando token", authentication.getName());
        String token = jwtGenerator.generateToken(authentication);
        JwtAuthResponseDTO jwtAuthResponseDTO = new JwtAuthResponseDTO();
        jwtAuthResponseDTO.setAccessToken(token);
        return new ResponseEntity<>(jwtAuthResponseDTO, HttpStatus.OK);
    }
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody  RegisterDTO registerDTO) {
        log.debug("POST /api/v1/auth/register - registro para username={}, email={}",
                registerDTO.getUsername(), registerDTO.getEmail());
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            log.debug("Registro rechazado: el username {} ya existe", registerDTO.getUsername());
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            log.debug("Registro rechazado: el email {} ya está en uso", registerDTO.getEmail());
            return new ResponseEntity<>("Email is already in use!", HttpStatus.BAD_REQUEST);
        }

         User newUser = userMapper.toUser(registerDTO);
         newUser.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
         userRepository.save(newUser);
        log.debug("Usuario registrado: id={}, username={}, email={}",
                newUser.getId(), newUser.getUsername(), newUser.getEmail());
        return new ResponseEntity<>("Usuario registrado", HttpStatus.OK);

    }




}
