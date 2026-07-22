package com.gestion.eventos.api.domain;

import jakarta.persistence.*;
import lombok.Data;

@Table(name="roles")
@Entity
@Data
public class Role {

 @Id
 @GeneratedValue(strategy= GenerationType.IDENTITY)
 private Long id;
 private String name;


}
