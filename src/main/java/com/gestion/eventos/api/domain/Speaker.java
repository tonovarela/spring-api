package com.gestion.eventos.api.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="speakers")
public class Speaker {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 100)
    private String name;
    @Column(nullable = false,length = 100)
    private String email;

   @Column(nullable = false,length = 500)
    private String bio;

   @ManyToMany(mappedBy = "speakers")
   private Set<Event> events = new HashSet<>();
}
