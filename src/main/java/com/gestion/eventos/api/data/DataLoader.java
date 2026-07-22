package com.gestion.eventos.api.data;


import com.gestion.eventos.api.domain.Role;
import com.gestion.eventos.api.domain.User;
import com.gestion.eventos.api.repository.IRoleRepository;
import com.gestion.eventos.api.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;


@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

private  final IRoleRepository roleRepository;
private final PasswordEncoder passwordEncoder;
private final IUserRepository userRepository;

    @Override
    @Transactional
    public void run(String ... args) throws Exception {
     Role adminRole =   roleRepository.findByName("ROLE_ADMIN").orElseGet(() ->
        {
            Role role = new Role();
            role.setName("ROLE_ADMIN");
            return roleRepository.save(role);
        });

   Role userRole  =  roleRepository.findByName("ROLE_USER").orElseGet(() ->
        {
            Role role = new Role();
            role.setName("ROLE_USER");
            return roleRepository.save(role);
        });

        if (userRepository.findByUsername("admin").isEmpty()) {
         User admin = new User();
         admin.setName("Admin");
         admin.setUsername("admin");
         admin.setEmail("admin@live.com");
         admin.setPassword(passwordEncoder.encode("admin1234"));
         Set<Role> adminRoles = new HashSet<Role>();
          adminRoles.add(adminRole);
          adminRoles.add(userRole);
         admin.setRoles(adminRoles);
         userRepository.save(admin);
            System.out.println("Admin user created successfully.");
        }else{
            System.out.println("Admin user already exists.");
        }


        if (userRepository.findByUsername("user").isEmpty()) {
            User user = new User();
            user.setName("User");
            user.setUsername("user");
            user.setEmail("user@live.com");
            user.setPassword(passwordEncoder.encode("user1234"));
            Set<Role> userRoles = new HashSet<Role>();
            userRoles.add(userRole);

            user.setRoles(userRoles);
            userRepository.save(user);
            System.out.println("User user created successfully.");
        }else{
            System.out.println("User user already exists.");
        }




    }




}
