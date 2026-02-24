package com.anas.back.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "demandes_teletravail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DemandeTeletravail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate dateDebut;

    @Column(nullable = false)
    private LocalDate dateFin;

    @Column(nullable = false)
    private LocalDateTime heure;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private StatutDemande statut;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TypeTeletravail type;

    @Column(columnDefinition = "TEXT")
    private String justification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employe_id", nullable = false)
    private User employe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private User manager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rh_id")
    private User rh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "politique_id")
    private PolitiqueTeletravail politique;

    @Column(columnDefinition = "TEXT")
    private String commentaireManager;

    @Column(columnDefinition = "TEXT")
    private String commentaireRH;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateModification;

    private LocalDateTime dateValidationManager;

    private LocalDateTime dateValidationRH;

    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
        heure = LocalDateTime.now();

        // Par défaut : EN_ATTENTE
        if (statut == null) {
            statut = StatutDemande.EN_ATTENTE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        dateModification = LocalDateTime.now();
    }

    // ========================
    // Méthodes métier
    // ========================

    public Boolean modifier() {
        if (this.statut == StatutDemande.EN_ATTENTE) {
            this.dateModification = LocalDateTime.now();
            return true;
        }
        return false;
    }

    public Boolean annuler() {
        if (this.statut != StatutDemande.VALIDEE &&
                this.statut != StatutDemande.ANNULEE) {

            this.statut = StatutDemande.ANNULEE;
            this.dateModification = LocalDateTime.now();
            return true;
        }
        return false;
    }

    public Boolean validerManager() {
        if (this.statut == StatutDemande.EN_ATTENTE) {
            this.dateValidationManager = LocalDateTime.now();
            return true;
        }
        return false;
    }

    public Boolean validerRH() {
        if (this.statut == StatutDemande.EN_ATTENTE) {
            this.statut = StatutDemande.VALIDEE;
            this.dateValidationRH = LocalDateTime.now();
            return true;
        }
        return false;
    }

    public Boolean rejeter() {
        if (this.statut == StatutDemande.EN_ATTENTE) {
            this.statut = StatutDemande.REJETEE;
            this.dateModification = LocalDateTime.now();
            return true;
        }
        return false;
    }
}