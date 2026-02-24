package com.anas.back.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "politique_teletravail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolitiqueTeletravail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int joursMaxParSemaine;
    private int joursMaxParMois;
    private int delaiNoticeJours;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rh_id", nullable = false)
    private User rh;
}