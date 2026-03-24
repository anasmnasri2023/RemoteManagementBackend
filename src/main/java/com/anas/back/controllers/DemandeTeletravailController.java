package com.anas.back.controllers;

import com.anas.back.model.DemandeTeletravail;
import com.anas.back.model.StatutDemande;
import com.anas.back.service.DemandeTeletravailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/demandes-teletravail")
@RequiredArgsConstructor
public class DemandeTeletravailController {

    private final DemandeTeletravailService service;

    // ════════════════════════════════════════════════════════════════════════
    // RÔLE : EMPLOYE
    // ════════════════════════════════════════════════════════════════════════

    /**
     * POST /api/demandes-teletravail/employe/{employeId}
     * Créer et soumettre une nouvelle demande de télétravail.
     * Statut initial automatique : EN_ATTENTE_MANAGER
     */
    @PostMapping("/employe/{employeId}")
    public ResponseEntity<DemandeTeletravail> creer(
            @PathVariable Long employeId,
            @RequestBody DemandeTeletravail demande) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.creer(demande, employeId));
    }

    /**
     * PUT /api/demandes-teletravail/{id}/employe
     * Modifier une demande (autorisé uniquement si EN_ATTENTE_MANAGER).
     */
    @PutMapping("/{id}/employe")
    public ResponseEntity<DemandeTeletravail> modifier(
            @PathVariable Long id,
            @RequestBody DemandeTeletravail demande) {
        return ResponseEntity.ok(service.modifier(id, demande));
    }

    /**
     * PATCH /api/demandes-teletravail/{id}/annuler
     * Annuler une demande (autorisé si EN_ATTENTE_MANAGER ou EN_ATTENTE_RH).
     */
    @PatchMapping("/{id}/annuler")
    public ResponseEntity<DemandeTeletravail> annuler(@PathVariable Long id) {
        return ResponseEntity.ok(service.annuler(id));
    }

    /**
     * GET /api/demandes-teletravail/employe/{employeId}
     * Consulter toutes ses propres demandes.
     */
    @GetMapping("/employe/{employeId}")
    public ResponseEntity<List<DemandeTeletravail>> getMesDemandes(
            @PathVariable Long employeId) {
        return ResponseEntity.ok(service.getMesDemandes(employeId));
    }

    // ════════════════════════════════════════════════════════════════════════
    // RÔLE : MANAGER — Validation N1
    // ════════════════════════════════════════════════════════════════════════

    /**
     * GET /api/demandes-teletravail/manager/en-attente
     * Voir toutes les demandes en attente de validation Manager (N1).
     */
    @GetMapping("/manager/en-attente")
    public ResponseEntity<List<DemandeTeletravail>> getDemandesEnAttenteManager() {
        return ResponseEntity.ok(service.getDemandesEnAttenteManager());
    }

    /**
     * PATCH /api/demandes-teletravail/{id}/valider-manager
     * Manager valide la demande → statut passe à EN_ATTENTE_RH.
     */
    @PatchMapping("/{id}/valider-manager")
    public ResponseEntity<DemandeTeletravail> validerManager(@PathVariable Long id) {
        return ResponseEntity.ok(service.validerManager(id));
    }

    /**
     * PATCH /api/demandes-teletravail/{id}/rejeter-manager
     * Manager rejette la demande → statut passe à REJETEE_MANAGER.
     */
    @PatchMapping("/{id}/rejeter-manager")
    public ResponseEntity<DemandeTeletravail> rejeterManager(@PathVariable Long id) {
        return ResponseEntity.ok(service.rejeterManager(id));
    }

    // ════════════════════════════════════════════════════════════════════════
    // RÔLE : RH — Validation N2
    // ════════════════════════════════════════════════════════════════════════

    /**
     * GET /api/demandes-teletravail/rh/en-attente
     * Voir toutes les demandes validées par Manager, en attente RH (N2).
     */
    @GetMapping("/rh/en-attente")
    public ResponseEntity<List<DemandeTeletravail>> getDemandesEnAttenteRH() {
        return ResponseEntity.ok(service.getDemandesEnAttenteRH());
    }

    /**
     * PATCH /api/demandes-teletravail/{id}/valider-rh
     * RH valide la demande → statut passe à APPROUVEE.
     * Prérequis : demande doit être EN_ATTENTE_RH (validée par Manager).
     */
    @PatchMapping("/{id}/valider-rh")
    public ResponseEntity<DemandeTeletravail> validerRH(@PathVariable Long id) {
        return ResponseEntity.ok(service.validerRH(id));
    }

    /**
     * PATCH /api/demandes-teletravail/{id}/rejeter-rh
     * RH rejette la demande → statut passe à REJETEE_RH.
     */
    @PatchMapping("/{id}/rejeter-rh")
    public ResponseEntity<DemandeTeletravail> rejeterRH(@PathVariable Long id) {
        return ResponseEntity.ok(service.rejeterRH(id));
    }

    // ════════════════════════════════════════════════════════════════════════
    // RÔLE : RH / ADMIN — Consultation globale
    // ════════════════════════════════════════════════════════════════════════

    /**
     * GET /api/demandes-teletravail
     * Consulter toutes les demandes (RH, Admin).
     */
    @GetMapping
    public ResponseEntity<List<DemandeTeletravail>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    /**
     * GET /api/demandes-teletravail/{id}
     * Consulter une demande par son ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DemandeTeletravail> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    /**
     * GET /api/demandes-teletravail/statut/{statut}
     * Filtrer les demandes par statut.
     */
    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<DemandeTeletravail>> getByStatut(
            @PathVariable StatutDemande statut) {
        return ResponseEntity.ok(service.getByStatut(statut));
    }

    /**
     * DELETE /api/demandes-teletravail/{id}
     * Supprimer une demande (Admin uniquement).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        service.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}