package com.anas.back.model;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private RoleNom nom;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public RoleNom getNom() { return nom; }
    public void setNom(RoleNom nom) { this.nom = nom; }
}