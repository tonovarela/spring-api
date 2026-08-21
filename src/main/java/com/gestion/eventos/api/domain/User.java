package com.gestion.eventos.api.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import tools.jackson.databind.annotation.JsonAppend;

import java.util.HashSet;
import java.util.Set;

@Table(name="users")
@Entity
@Data
public class User{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String username;
    private String email;
    private String password;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name="user_id",referencedColumnName = "id"),
            inverseJoinColumns =@JoinColumn(name="role_id",referencedColumnName = "id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Role> roles = new HashSet<Role>();

    @ManyToMany(fetch = FetchType.LAZY ,cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(
            name = "user_attended_events",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private Set<Event> attendedEvents = new HashSet<>();


    public void addAttendedEvent(Event event) {
        this.attendedEvents.add(event);
        event.getAttendedUsers().add(this);
    }

    public void removeAttendedEvent(Event event) {
        this.attendedEvents.remove(event);
        event.getAttendedUsers().remove(this);
    }
}


