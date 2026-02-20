package com.anas.back.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rapports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rapport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;

    private LocalDateTime dateGeneration;

    @ManyToOne
    @JoinColumn(name = "rh_id")
    private User rh;
}