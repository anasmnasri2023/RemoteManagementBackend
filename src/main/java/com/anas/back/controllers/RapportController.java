package com.anas.back.controllers;

import com.anas.back.model.Rapport;
import com.anas.back.service.RapportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rapports")
@CrossOrigin("*")
public class RapportController {

    @Autowired
    private RapportService rapportService;

    @GetMapping
    public ResponseEntity<List<Rapport>> getAllRapports() {
        return ResponseEntity.ok(rapportService.getAllRapports());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rapport> getRapportById(@PathVariable Long id) {
        return ResponseEntity.ok(rapportService.getRapportById(id));
    }

    @GetMapping("/rh/{rhId}")
    public ResponseEntity<List<Rapport>> getRapportsByRh(@PathVariable Long rhId) {
        return ResponseEntity.ok(rapportService.getRapportsByRh(rhId));
    }

    @PostMapping
    public ResponseEntity<Rapport> createRapport(@RequestBody Rapport rapport) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(rapportService.createRapport(rapport));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rapport> updateRapport(@PathVariable Long id,
                                                 @RequestBody Rapport rapport) {
        return ResponseEntity.ok(rapportService.updateRapport(id, rapport));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRapport(@PathVariable Long id) {
        rapportService.deleteRapport(id);
        return ResponseEntity.noContent().build();
    }
}