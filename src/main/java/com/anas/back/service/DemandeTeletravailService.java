package com.anas.back.service;

import com.anas.back.model.DemandeTeletravail;

import java.util.List;

public interface DemandeTeletravailService {

    DemandeTeletravail creer(DemandeTeletravail demande);

    DemandeTeletravail modifier(Long id, DemandeTeletravail demande);

    void supprimer(Long id);

    DemandeTeletravail getById(Long id);

    List<DemandeTeletravail> getAll();

    List<DemandeTeletravail> getByEmploye(Long employeId);

    DemandeTeletravail validerRH(Long id);

    DemandeTeletravail rejeter(Long id);

    DemandeTeletravail annuler(Long id);
}