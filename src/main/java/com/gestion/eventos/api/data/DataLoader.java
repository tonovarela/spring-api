package com.gestion.eventos.api.data;


import com.gestion.eventos.api.domain.*;
import com.gestion.eventos.api.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ICategoryRepository categoryRepository;
    private final ISpeakerRepository speakerRepository;
    private final IEventRepository eventRepository; // ¡Inyecta el EventRepository!


    @Override
    @Transactional
    public void run(String ... args) throws Exception {

            log.debug("run() - iniciando carga de datos iniciales");



            // --- LÓGICA EXISTENTE PARA ROLES Y USUARIOS ---
            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet( () -> {
                        Role newRole = new Role();
                        newRole.setName("ROLE_ADMIN");
                        log.info("Rol 'ROLE_ADMIN' creado.");
                        return roleRepository.save(newRole);
                    });

            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseGet(() -> {
                        Role newRole = new Role();
                        newRole.setName("ROLE_USER");
                        log.info("Rol 'ROLE_USER' creado.");
                        return roleRepository.save(newRole);
                    });

            if(userRepository.findByUsername("admin").isEmpty()){
                User admin = new User();
                admin.setName("Administrador");
                admin.setUsername("admin");
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("admin1234"));

                Set<Role> adminRoles = new HashSet<>();
                adminRoles.add(adminRole);
                adminRoles.add(userRole);

                admin.setRoles(adminRoles);

                userRepository.save(admin);
                log.info("Usuario 'admin' creado.");
            }

            if (userRepository.findByUsername("user").isEmpty()) {
                User regularUser = new User();
                regularUser.setName("Usuario Normal");
                regularUser.setUsername("user");
                regularUser.setEmail("user@example.com");
                regularUser.setPassword(passwordEncoder.encode("123456"));

                Set<Role> userRoles = new HashSet<>();
                userRoles.add(userRole);
                regularUser.setRoles(userRoles);

                userRepository.save(regularUser);
                log.info("Usuario 'user' creado.");
            }

            // --- LÓGICA EXISTENTE PARA CATEGORÍAS ---
            Category conferencia = categoryRepository.findByName("Conferencia")
                    .orElseGet(() -> {
                        Category newCat = new Category(null, "Conferencia", "Eventos de gran escala con múltiples oradores.");
                        log.info("Categoría 'Conferencia' creada.");
                        return categoryRepository.save(newCat);
                    });
            Category taller = categoryRepository.findByName("Taller")
                    .orElseGet(() -> {
                        Category newCat = new Category(null, "Taller", "Eventos interactivos y prácticos.");
                        log.info("Categoría 'Taller' creada.");
                        return categoryRepository.save(newCat);
                    });
            Category webinar = categoryRepository.findByName("Webinar")
                    .orElseGet(() -> {
                        Category newCat = new Category(null, "Webinar", "Seminarios online en vivo.");
                        log.info("Categoría 'Webinar' creada.");
                        return categoryRepository.save(newCat);
                    });
            // --- También puedes usar existsByName para verificar antes de crear como lo tenías.
            // --- Lo he cambiado a findByName().orElseGet() para obtener las instancias guardadas
            // --- que necesitaremos para los eventos.


            // --- LÓGICA EXISTENTE PARA ORADORES ---
            Speaker john = speakerRepository.findByEmail("john.doe@example.com")
                    .orElseGet(() -> {
                        Speaker newSpeaker = new Speaker(null, "John Doe", "john.doe@example.com", "Experto en desarrollo de software.", new HashSet<>());
                        log.info("Speaker 'John Doe' creado.");
                        return speakerRepository.save(newSpeaker);
                    });
            Speaker jane = speakerRepository.findByEmail("jane.smith@example.com")
                    .orElseGet(() -> {
                        Speaker newSpeaker = new Speaker(null, "Jane Smith", "jane.smith@example.com", "Especialista en marketing digital.", new HashSet<>());
                        log.info("Speaker 'Jane Smith' creado.");
                        return speakerRepository.save(newSpeaker);
                    });
            // Asegúrate de que los repositorios de Category y Speaker tengan métodos findByName y findByEmail respectivamente.
            // Si no los tienen, añádelos:
            // CategoryRepository: Optional<Category> findByName(String name);
            // SpeakerRepository: Optional<Speaker> findByEmail(String email);


            // --- NUEVA LÓGICA PARA CREAR Y GUARDAR EVENTOS ---
            long existingEvents = eventRepository.count();
            log.debug("run() - eventos existentes en base de datos: {}", existingEvents);
            if (existingEvents == 0) { // Solo cargar eventos si la tabla está vacía
                List<Event> events = new ArrayList<>();
                LocalDate baseDate = LocalDate.now();

                for (int i = 1; i <= 60; i++) { // Cambia 60 al número deseado de eventos
                    Event event = new Event();
                    event.setName("Evento " + (i < 10 ? "0" + i : i) + ": Conferencia de Tecnología " + (i % 5 + 1));
                    event.setDate(baseDate.plusDays(i)); // Fechas futuras
                    event.setLocal("Sala " + (i % 10 + 1)); // 10 localizaciones diferentes

                    // Asignar una categoría
                    if (i % 3 == 0) {
                        event.setCategory(conferencia);
                    } else if (i % 3 == 1) {
                        event.setCategory(taller);
                    } else {
                        event.setCategory(webinar);
                    }

                    // Asignar al menos un orador
                    if (i % 2 == 0) {
                        event.addSpeakers(john); // Usamos el método addSpeaker para manejar la relación
                    } else {
                        event.addSpeakers(jane);
                    }
                    // Si quieres que algunos tengan ambos oradores:
                    if (i % 5 == 0) {
                        event.addSpeakers(john);
                        event.addSpeakers(jane);
                    }


                    events.add(event);
                }

                eventRepository.saveAll(events);
                log.info("Cargados {} eventos de prueba en la base de datos.", events.size());


            }

            log.debug("run() - carga de datos iniciales finalizada");

    }




}
