package com.anas.back.repository;

import com.anas.back.model.DemandeTeletravail;
import com.anas.back.model.StatutDemande;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DemandeTeletravailRepository extends JpaRepository<DemandeTeletravail, Long> {

    List<DemandeTeletravail> findByEmployeId(Long employeId);

    List<DemandeTeletravail> findByStatut(StatutDemande statut);
}