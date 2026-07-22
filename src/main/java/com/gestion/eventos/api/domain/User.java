package com.gestion.eventos.api.domain;


import jakarta.persistence.*;
import lombok.Data;

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
    private Set<Role> roles = new HashSet<Role>();
}


