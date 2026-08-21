package com.gestion.eventos.api.domain;

import jakarta.persistence.*;
import lombok.Data;

@Table(name="roles")
@Entity
@Data
public class Role {

 @Id  @GeneratedValue(strategy= GenerationType.IDENTITY)
 private Long id;
 @Column(nullable = false,unique = true)
 private String name;


}
