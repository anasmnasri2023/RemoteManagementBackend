package com.anas.back.controllers;

import com.anas.back.model.DemandeTeletravail;
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

    // ── CREATE ──────────────────────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<DemandeTeletravail> creer(@RequestBody DemandeTeletravail demande) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.creer(demande));
    }

    // ── READ ─────────────────────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<List<DemandeTeletravail>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DemandeTeletravail> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/employe/{employeId}")
    public ResponseEntity<List<DemandeTeletravail>> getByEmploye(@PathVariable Long employeId) {
        return ResponseEntity.ok(service.getByEmploye(employeId));
    }

    // ── UPDATE ───────────────────────────────────────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<DemandeTeletravail> modifier(
            @PathVariable Long id,
            @RequestBody DemandeTeletravail demande) {
        return ResponseEntity.ok(service.modifier(id, demande));
    }

    // ── DELETE ───────────────────────────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        service.supprimer(id);
        return ResponseEntity.noContent().build();
    }

    // ── ACTIONS MÉTIER ───────────────────────────────────────────────────────
    @PatchMapping("/{id}/valider-rh")
    public ResponseEntity<DemandeTeletravail> validerRH(@PathVariable Long id) {
        return ResponseEntity.ok(service.validerRH(id));
    }

    @PatchMapping("/{id}/rejeter")
    public ResponseEntity<DemandeTeletravail> rejeter(@PathVariable Long id) {
        return ResponseEntity.ok(service.rejeter(id));
    }

    @PatchMapping("/{id}/annuler")
    public ResponseEntity<DemandeTeletravail> annuler(@PathVariable Long id) {
        return ResponseEntity.ok(service.annuler(id));
    }
}