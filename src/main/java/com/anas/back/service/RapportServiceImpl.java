package com.anas.back.service;

import com.anas.back.model.Rapport;
import com.anas.back.repository.RapportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RapportServiceImpl implements RapportService {

    @Autowired
    private RapportRepository rapportRepository;

    @Override
    public List<Rapport> getAllRapports() {
        return rapportRepository.findAll();
    }

    @Override
    public Rapport getRapportById(Long id) {
        return rapportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rapport non trouvé avec l'id : " + id));
    }

    @Override
    public Rapport createRapport(Rapport rapport) {
        rapport.setDateGeneration(LocalDateTime.now());
        return rapportRepository.save(rapport);
    }

    @Override
    public Rapport updateRapport(Long id, Rapport rapport) {
        Rapport existing = getRapportById(id);
        if (rapport.getTitre() != null) {
            existing.setTitre(rapport.getTitre());
        }
        return rapportRepository.save(existing);
    }

    @Override
    public void deleteRapport(Long id) {
        rapportRepository.deleteById(id);
    }

    @Override
    public List<Rapport> getRapportsByRh(Long rhId) {
        return rapportRepository.findByRhId(rhId);
    }
}