package com.anas.back.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
    @CreationTimestamp
    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;
    // ── Champs du diagramme ───────────────────────────────────────────────────
    @Column(nullable = false)
    private LocalDate dateDebut;

    @Column(nullable = false)
    private LocalDate dateFin;

    private LocalTime heure;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatutDemande statut = StatutDemande.EN_ATTENTE_MANAGER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeTeletravail type;

    private String justification;

    // ── Relations ─────────────────────────────────────────────────────────────
    // L'employé qui a créé la demande (lié à User via son rôle EMPLOYE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employe_id", nullable = false)
    private User employe;

    // ── Méthodes métier du diagramme ──────────────────────────────────────────

    /**
     * Soumettre() : Boolean
     * L'employé soumet la demande → statut initial EN_ATTENTE_MANAGER
     */
    public boolean soumettre() {
        // Une demande ne peut être soumise que si elle est nouvelle (pas encore de statut)
        if (this.statut == null || this.statut == StatutDemande.EN_ATTENTE_MANAGER) {
            this.statut = StatutDemande.EN_ATTENTE_MANAGER;
            return true;
        }
        return false;
    }

    /**
     * Modifier() : Boolean
     * L'employé peut modifier uniquement si la demande est encore EN_ATTENTE_MANAGER
     */
    public boolean modifier() {
        return this.statut == StatutDemande.EN_ATTENTE_MANAGER;
    }

    /**
     * Annuler() : Boolean
     * L'employé peut annuler si la demande est EN_ATTENTE_MANAGER ou EN_ATTENTE_RH
     */
    public boolean annuler() {
        if (this.statut == StatutDemande.EN_ATTENTE_MANAGER
                || this.statut == StatutDemande.EN_ATTENTE_RH) {
            this.statut = StatutDemande.ANNULEE;
            return true;
        }
        return false;
    }

    /**
     * validerManager() : Boolean — Validation N1 par le Manager
     * Si accepté → passe à EN_ATTENTE_RH
     */
    public boolean validerManager() {
        if (this.statut == StatutDemande.EN_ATTENTE_MANAGER) {
            this.statut = StatutDemande.EN_ATTENTE_RH;
            return true;
        }
        return false;
    }

    /**
     * rejeterManager() : Boolean — Rejet N1 par le Manager
     */
    public boolean rejeterManager() {
        if (this.statut == StatutDemande.EN_ATTENTE_MANAGER) {
            this.statut = StatutDemande.REJETEE_MANAGER;
            return true;
        }
        return false;
    }

    /**
     * validerRH() : Boolean — Validation N2 par le RH
     * Seulement si déjà validée par le Manager (EN_ATTENTE_RH)
     */
    public boolean validerRH() {
        if (this.statut == StatutDemande.EN_ATTENTE_RH) {
            this.statut = StatutDemande.APPROUVEE;
            return true;
        }
        return false;
    }

    /**
     * rejeterRH() : Boolean — Rejet N2 par le RH
     */
    public boolean rejeterRH() {
        if (this.statut == StatutDemande.EN_ATTENTE_RH) {
            this.statut = StatutDemande.REJETEE_RH;
            return true;
        }
        return false;
    }
}