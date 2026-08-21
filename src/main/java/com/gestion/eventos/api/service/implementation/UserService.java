package com.gestion.eventos.api.service.implementation;

import com.gestion.eventos.api.domain.Role;
import com.gestion.eventos.api.domain.User;
import com.gestion.eventos.api.exception.ResouceNotFoundException;
import com.gestion.eventos.api.repository.IUserRepository;


import com.gestion.eventos.api.security.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {

   private final IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.debug("loadUserByUsername() - cargando usuario username={}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> {
                    log.debug("loadUserByUsername() - usuario no encontrado con username={}", username);
                    return new UsernameNotFoundException("Usuario no encontrado con username: " + username);
                }
        );
        log.debug("loadUserByUsername() - usuario username={} cargado con {} rol(es)", username, user.getRoles().size());
       return new org.springframework.security.core.userdetails.User(
               user.getUsername(),user.getPassword(), mapRolesToAuthorities(user.getRoles())
       );
    }

    @Override
    public List<User> findAll() {
        log.debug("findAll() - consultando todos los usuarios");
        List<User> users = userRepository.findAll();
        log.debug("findAll() - usuarios recuperados: {}", users.size());
        return users;
    }

    @Override
    public User save(User event) {
        log.debug("save() - guardando usuario username={}", event.getUsername());
        User savedUser = userRepository.save(event);
        log.debug("save() - usuario guardado con id={}", savedUser.getId());
        return savedUser;
    }

    @Override
    public User findById(Long id) {
        log.debug("findById() - buscando usuario con id={}", id);
        return userRepository.findById(id).orElseThrow(()-> {
            log.debug("findById() - usuario no encontrado con id={}", id);
            return new ResouceNotFoundException("Usuario no encontrado con id " + id);
        });
    }

    @Override
    public void deleteById(Long id) {
        log.debug("deleteById() - eliminando usuario id={}", id);
         userRepository.deleteById(id);
        log.debug("deleteById() - usuario id={} eliminado", id);
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(r-> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }
}
