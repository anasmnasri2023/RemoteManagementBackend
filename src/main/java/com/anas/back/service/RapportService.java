package com.anas.back.service;

import com.anas.back.model.Rapport;
import java.util.List;

public interface RapportService {
    List<Rapport> getAllRapports();
    Rapport getRapportById(Long id);
    Rapport createRapport(Rapport rapport);
    Rapport updateRapport(Long id, Rapport rapport);
    void deleteRapport(Long id);
    List<Rapport> getRapportsByRh(Long rhId);
}