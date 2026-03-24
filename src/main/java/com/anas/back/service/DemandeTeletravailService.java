package com.anas.back.service;

import com.anas.back.model.DemandeTeletravail;
import com.anas.back.model.StatutDemande;

import java.util.List;

public interface DemandeTeletravailService {

    // ── EMPLOYE ───────────────────────────────────────────────────────────────
    /** Créer et soumettre une nouvelle demande (rôle : EMPLOYE) */
    DemandeTeletravail creer(DemandeTeletravail demande, Long employeId);

    /** Modifier une demande EN_ATTENTE_MANAGER (rôle : EMPLOYE) */
    DemandeTeletravail modifier(Long id, DemandeTeletravail demande);

    /** Annuler une demande (rôle : EMPLOYE) */
    DemandeTeletravail annuler(Long id);

    /** Consulter ses propres demandes (rôle : EMPLOYE) */
    List<DemandeTeletravail> getMesDemandes(Long employeId);

    // ── MANAGER — Validation N1 ───────────────────────────────────────────────
    /** Valider au niveau Manager → passe à EN_ATTENTE_RH (rôle : MANAGER) */
    DemandeTeletravail validerManager(Long id);

    /** Rejeter au niveau Manager (rôle : MANAGER) */
    DemandeTeletravail rejeterManager(Long id);

    /** Voir toutes les demandes en attente de validation Manager */
    List<DemandeTeletravail> getDemandesEnAttenteManager();

    // ── RH — Validation N2 ───────────────────────────────────────────────────
    /** Valider au niveau RH → APPROUVEE (rôle : RH) */
    DemandeTeletravail validerRH(Long id);

    /** Rejeter au niveau RH (rôle : RH) */
    DemandeTeletravail rejeterRH(Long id);

    /** Voir toutes les demandes en attente de validation RH */
    List<DemandeTeletravail> getDemandesEnAttenteRH();

    /** Consulter toutes les demandes (rôle : RH / ADMIN) */
    List<DemandeTeletravail> getAll();

    // ── COMMUN ────────────────────────────────────────────────────────────────
    DemandeTeletravail getById(Long id);

    List<DemandeTeletravail> getByStatut(StatutDemande statut);

    void supprimer(Long id);
}