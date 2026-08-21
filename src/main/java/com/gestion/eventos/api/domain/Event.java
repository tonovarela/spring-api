package com.gestion.eventos.api.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
@Entity
@Table(name="events")
public class Event {


    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false)
    private String name;
    @Column(nullable=false)
    private LocalDate date;
    @Column(nullable=false)
    private String local;

    @ManyToMany(fetch = FetchType.LAZY ,cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(
            name="event_speakers",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "speaker_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Speaker> speakers= new HashSet<>();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Category category;

    @ManyToMany(mappedBy = "attendedEvents",fetch = FetchType.LAZY ,cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<User> attendedUsers = new HashSet<>();

    public void addSpeakers(Speaker speaker){
        speakers.add(speaker);
        speaker.getEvents().add(this);
    }

    public void removeSpeakers(Speaker speaker){
        speakers.remove(speaker);
        speaker.getEvents().remove(this);
    }
}
