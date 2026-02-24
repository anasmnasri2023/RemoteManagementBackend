package com.anas.back.service;

import com.anas.back.model.PolitiqueTeletravail;
import com.anas.back.model.RoleNom;
import com.anas.back.model.User;
import com.anas.back.repository.PolitiqueTeletravailRepository;
import com.anas.back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PolitiqueTeletravailServiceImpl implements PolitiqueTeletravailService {

    private final PolitiqueTeletravailRepository politiqueRepository;
    private final UserRepository userRepository;

    @Override
    public PolitiqueTeletravail creer(PolitiqueTeletravail politique, Long rhId) {
        User rh = userRepository.findById(rhId)
                .orElseThrow(() -> new RuntimeException("RH introuvable avec l'id: " + rhId));

        if (rh.getRole() != RoleNom.RH) {
            throw new RuntimeException("L'utilisateur n'est pas un RH");
        }

        politique.setRh(rh);
        return politiqueRepository.save(politique);
    }

    @Override
    public List<PolitiqueTeletravail> getToutesLesPolitiques() {
        return politiqueRepository.findAll();
    }

    @Override
    public List<PolitiqueTeletravail> getPolitiquesByRh(Long rhId) {
        return politiqueRepository.findByRhId(rhId);
    }

    @Override
    public PolitiqueTeletravail getById(Long id) {
        return politiqueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Politique introuvable avec l'id: " + id));
    }

    @Override
    public PolitiqueTeletravail modifier(Long id, PolitiqueTeletravail updated) {
        PolitiqueTeletravail politique = getById(id);
        politique.setJoursMaxParSemaine(updated.getJoursMaxParSemaine());
        politique.setJoursMaxParMois(updated.getJoursMaxParMois());
        politique.setDelaiNoticeJours(updated.getDelaiNoticeJours());
        return politiqueRepository.save(politique);
    }

    @Override
    public void supprimer(Long id) {
        politiqueRepository.deleteById(id);
    }

    @Override
    public boolean valider(Long id) {
        PolitiqueTeletravail politique = getById(id);
        return politique.getJoursMaxParSemaine() > 0
                && politique.getJoursMaxParMois() > 0
                && politique.getDelaiNoticeJours() >= 0
                && politique.getJoursMaxParSemaine() <= 5
                && politique.getJoursMaxParMois() >= politique.getJoursMaxParSemaine();
    }
}

