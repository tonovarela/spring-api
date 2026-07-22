package com.gestion.eventos.api.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;


@Data
@Entity
@Table(name="events")
public class Event {


    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDate date;
    private String local;
}
