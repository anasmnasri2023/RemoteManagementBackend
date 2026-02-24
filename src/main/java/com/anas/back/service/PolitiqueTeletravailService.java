package com.anas.back.service;

import com.anas.back.model.PolitiqueTeletravail;

import java.util.List;

public interface PolitiqueTeletravailService {

    PolitiqueTeletravail creer(PolitiqueTeletravail politique, Long rhId);

    List<PolitiqueTeletravail> getToutesLesPolitiques();

    List<PolitiqueTeletravail> getPolitiquesByRh(Long rhId);

    PolitiqueTeletravail getById(Long id);

    PolitiqueTeletravail modifier(Long id, PolitiqueTeletravail updated);

    void supprimer(Long id);

    boolean valider(Long id);
}