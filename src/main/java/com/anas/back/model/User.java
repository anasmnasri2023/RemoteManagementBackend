package com.anas.back.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;

    @Column(unique = true, nullable = false)
    private String email;

    private int telephone;

    @Enumerated(EnumType.STRING)
    private RoleNom role;

    @Column(nullable = false)
    private String motDePasse;           // ✅ Stocké en DB haché

    @Column(nullable = false)
    private String confirmeMotDePasse;   // ✅ Stocké en DB haché aussi
}