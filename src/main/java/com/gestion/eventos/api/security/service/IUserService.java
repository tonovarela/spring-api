package com.gestion.eventos.api.security.service;

import com.gestion.eventos.api.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;


public interface IUserService extends UserDetailsService {

    List<User> findAll();
    User save(User event);
    User findById(Long id);
    void deleteById(Long id);

}
