package com.anas.back.service;

import com.anas.back.model.DemandeTeletravail;
import com.anas.back.repository.DemandeTeletravailRepository;
import com.anas.back.service.DemandeTeletravailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DemandeTeletravailServiceImpl implements DemandeTeletravailService {

    private final DemandeTeletravailRepository repository;

    @Override
    public DemandeTeletravail creer(DemandeTeletravail demande) {
        return repository.save(demande);
    }

    @Override
    public DemandeTeletravail modifier(Long id, DemandeTeletravail demande) {
        DemandeTeletravail existing = getById(id);

        if (!existing.modifier()) {
            throw new RuntimeException("Modification non autorisée");
        }

        existing.setDateDebut(demande.getDateDebut());
        existing.setDateFin(demande.getDateFin());
        existing.setType(demande.getType());
        existing.setJustification(demande.getJustification());

        return repository.save(existing);
    }

    @Override
    public void supprimer(Long id) {
        repository.deleteById(id);
    }

    @Override
    public DemandeTeletravail getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));
    }

    @Override
    public List<DemandeTeletravail> getAll() {
        return repository.findAll();
    }

    @Override
    public List<DemandeTeletravail> getByEmploye(Long employeId) {
        return repository.findByEmployeId(employeId);
    }

    @Override
    public DemandeTeletravail validerRH(Long id) {
        DemandeTeletravail demande = getById(id);

        if (!demande.validerRH()) {
            throw new RuntimeException("Validation impossible");
        }

        return repository.save(demande);
    }

    @Override
    public DemandeTeletravail rejeter(Long id) {
        DemandeTeletravail demande = getById(id);

        if (!demande.rejeter()) {
            throw new RuntimeException("Rejet impossible");
        }

        return repository.save(demande);
    }

    @Override
    public DemandeTeletravail annuler(Long id) {
        DemandeTeletravail demande = getById(id);

        if (!demande.annuler()) {
            throw new RuntimeException("Annulation impossible");
        }

        return repository.save(demande);
    }
}