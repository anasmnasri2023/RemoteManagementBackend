package com.anas.back.service;

import com.anas.back.model.DemandeTeletravail;
import com.anas.back.model.StatutDemande;
import com.anas.back.model.User;
import com.anas.back.repository.DemandeTeletravailRepository;
import com.anas.back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DemandeTeletravailServiceImpl implements DemandeTeletravailService {

    private final DemandeTeletravailRepository repository;
    private final UserRepository userRepository;

    // ── EMPLOYE ───────────────────────────────────────────────────────────────

    /**
     * L'employé crée et soumet une demande.
     * Le statut initial est automatiquement EN_ATTENTE_MANAGER.
     */
    @Override
    public DemandeTeletravail creer(DemandeTeletravail demande, Long employeId) {
        User employe = userRepository.findById(employeId)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé avec id : " + employeId));

        demande.setEmploye(employe);
        demande.setStatut(StatutDemande.EN_ATTENTE_MANAGER); // Flux : étape 1
        return repository.save(demande);
    }

    /**
     * L'employé modifie sa demande.
     * Autorisé uniquement si statut = EN_ATTENTE_MANAGER.
     */
    @Override
    public DemandeTeletravail modifier(Long id, DemandeTeletravail demande) {
        DemandeTeletravail existing = getById(id);

        if (!existing.modifier()) {
            throw new RuntimeException(
                    "Modification impossible : la demande est en statut [" + existing.getStatut() + "]. "
                            + "Seules les demandes EN_ATTENTE_MANAGER peuvent être modifiées."
            );
        }

        existing.setDateDebut(demande.getDateDebut());
        existing.setDateFin(demande.getDateFin());
        existing.setHeure(demande.getHeure());
        existing.setType(demande.getType());
        existing.setJustification(demande.getJustification());

        return repository.save(existing);
    }

    /**
     * L'employé annule sa demande.
     * Autorisé si statut = EN_ATTENTE_MANAGER ou EN_ATTENTE_RH.
     */
    @Override
    public DemandeTeletravail annuler(Long id) {
        DemandeTeletravail demande = getById(id);

        if (!demande.annuler()) {
            throw new RuntimeException(
                    "Annulation impossible : la demande est en statut [" + demande.getStatut() + "]. "
                            + "Seules les demandes en attente peuvent être annulées."
            );
        }

        return repository.save(demande);
    }

    /**
     * L'employé consulte ses propres demandes.
     */
    @Override
    public List<DemandeTeletravail> getMesDemandes(Long employeId) {
        return repository.findByEmployeId(employeId);
    }

    // ── MANAGER — Validation N1 ───────────────────────────────────────────────

    /**
     * Le Manager valide la demande au niveau N1.
     * Statut : EN_ATTENTE_MANAGER → EN_ATTENTE_RH
     */
    @Override
    public DemandeTeletravail validerManager(Long id) {
        DemandeTeletravail demande = getById(id);

        if (!demande.validerManager()) {
            throw new RuntimeException(
                    "Validation Manager impossible : la demande est en statut [" + demande.getStatut() + "]. "
                            + "Seules les demandes EN_ATTENTE_MANAGER peuvent être validées par le Manager."
            );
        }

        return repository.save(demande); // statut devient EN_ATTENTE_RH
    }

    /**
     * Le Manager rejette la demande au niveau N1.
     * Statut : EN_ATTENTE_MANAGER → REJETEE_MANAGER
     */
    @Override
    public DemandeTeletravail rejeterManager(Long id) {
        DemandeTeletravail demande = getById(id);

        if (!demande.rejeterManager()) {
            throw new RuntimeException(
                    "Rejet Manager impossible : la demande est en statut [" + demande.getStatut() + "]. "
                            + "Seules les demandes EN_ATTENTE_MANAGER peuvent être rejetées par le Manager."
            );
        }

        return repository.save(demande); // statut devient REJETEE_MANAGER
    }

    /**
     * Liste des demandes en attente de validation par le Manager.
     */
    @Override
    public List<DemandeTeletravail> getDemandesEnAttenteManager() {
        return repository.findByStatut(StatutDemande.EN_ATTENTE_MANAGER);
    }

    // ── RH — Validation N2 ───────────────────────────────────────────────────

    /**
     * Le RH valide la demande au niveau N2.
     * Statut : EN_ATTENTE_RH → APPROUVEE
     * Prérequis : doit avoir été validée par le Manager d'abord.
     */
    @Override
    public DemandeTeletravail validerRH(Long id) {
        DemandeTeletravail demande = getById(id);

        if (!demande.validerRH()) {
            throw new RuntimeException(
                    "Validation RH impossible : la demande est en statut [" + demande.getStatut() + "]. "
                            + "Seules les demandes EN_ATTENTE_RH (déjà validées par Manager) peuvent être validées par le RH."
            );
        }

        return repository.save(demande); // statut devient APPROUVEE
    }

    /**
     * Le RH rejette la demande au niveau N2.
     * Statut : EN_ATTENTE_RH → REJETEE_RH
     */
    @Override
    public DemandeTeletravail rejeterRH(Long id) {
        DemandeTeletravail demande = getById(id);

        if (!demande.rejeterRH()) {
            throw new RuntimeException(
                    "Rejet RH impossible : la demande est en statut [" + demande.getStatut() + "]. "
                            + "Seules les demandes EN_ATTENTE_RH peuvent être rejetées par le RH."
            );
        }

        return repository.save(demande); // statut devient REJETEE_RH
    }

    /**
     * Liste des demandes en attente de validation RH.
     */
    @Override
    public List<DemandeTeletravail> getDemandesEnAttenteRH() {
        return repository.findByStatut(StatutDemande.EN_ATTENTE_RH);
    }

    // ── COMMUN ────────────────────────────────────────────────────────────────

    @Override
    public List<DemandeTeletravail> getAll() {
        return repository.findAll();
    }

    @Override
    public DemandeTeletravail getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée avec id : " + id));
    }

    @Override
    public List<DemandeTeletravail> getByStatut(StatutDemande statut) {
        return repository.findByStatut(statut);
    }

    @Override
    public void supprimer(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Demande non trouvée avec id : " + id);
        }
        repository.deleteById(id);
    }
}