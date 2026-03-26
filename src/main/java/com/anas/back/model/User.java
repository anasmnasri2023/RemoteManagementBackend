package com.anas.back.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    // Doit commencer par une majuscule, lettres seulement (accents inclus)
    @NotBlank(message = "Le nom est obligatoire")
    @Pattern(
            regexp = "^[A-ZÀÂÆÇÉÈÊËÎÏÔŒÙÛÜŸ][a-zA-ZÀ-ÿ' -]*$",
            message = "Le nom doit commencer par une majuscule et contenir uniquement des lettres"
    )
    private String nom;

    // Doit commencer par une majuscule, lettres seulement (accents inclus)
    @NotBlank(message = "Le prénom est obligatoire")
    @Pattern(
            regexp = "^[A-ZÀÂÆÇÉÈÊËÎÏÔŒÙÛÜŸ][a-zA-ZÀ-ÿ' -]*$",
            message = "Le prénom doit commencer par une majuscule et contenir uniquement des lettres"
    )
    private String prenom;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être une adresse valide (ex: user@example.com)")
    private String email;

    // int : exactement 8 chiffres => entre 10_000_000 et 99_999_999
    @Min(value = 10_000_000, message = "Le téléphone doit contenir exactement 8 chiffres")
    @Max(value = 99_999_999, message = "Le téléphone doit contenir exactement 8 chiffres")
    private int telephone;

    @Enumerated(EnumType.STRING)
    private RoleNom role;

    // Min 8 chars, au moins : 1 majuscule, 1 minuscule, 1 chiffre, 1 symbole
    @Column(nullable = false)
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?`~])[\\S]{8,}$",
            message = "Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule, un chiffre et un symbole"
    )
    private String motDePasse;

    @Column(nullable = false)
    @NotBlank(message = "La confirmation du mot de passe est obligatoire")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?`~])[\\S]{8,}$",
            message = "La confirmation doit contenir au moins 8 caractères, une majuscule, une minuscule, un chiffre et un symbole"
    )
    private String confirmeMotDePasse;
}