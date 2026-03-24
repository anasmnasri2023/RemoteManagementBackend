package com.anas.back.repository;

import com.anas.back.model.DemandeTeletravail;
import com.anas.back.model.StatutDemande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandeTeletravailRepository extends JpaRepository<DemandeTeletravail, Long> {

    // Toutes les demandes d'un employé
    List<DemandeTeletravail> findByEmployeId(Long employeId);

    // Demandes filtrées par statut (utile pour Manager et RH)
    List<DemandeTeletravail> findByStatut(StatutDemande statut);

    // Demandes d'un employé filtrées par statut
    List<DemandeTeletravail> findByEmployeIdAndStatut(Long employeId, StatutDemande statut);
}