package com.anas.back.controllers;

import com.anas.back.model.PolitiqueTeletravail;
import com.anas.back.service.PolitiqueTeletravailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/politiques-teletravail")
@RequiredArgsConstructor
public class PolitiqueTeletravailController {

    private final PolitiqueTeletravailService politiqueService;

    @PostMapping("/{rhId}")
    public ResponseEntity<PolitiqueTeletravail> creer(
            @RequestBody PolitiqueTeletravail politique,
            @PathVariable Long rhId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(politiqueService.creer(politique, rhId));
    }

    @GetMapping
    public ResponseEntity<List<PolitiqueTeletravail>> getAll() {
        return ResponseEntity.ok(politiqueService.getToutesLesPolitiques());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PolitiqueTeletravail> getById(@PathVariable Long id) {
        return ResponseEntity.ok(politiqueService.getById(id));
    }

    @GetMapping("/rh/{rhId}")
    public ResponseEntity<List<PolitiqueTeletravail>> getByRh(@PathVariable Long rhId) {
        return ResponseEntity.ok(politiqueService.getPolitiquesByRh(rhId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PolitiqueTeletravail> modifier(
            @PathVariable Long id,
            @RequestBody PolitiqueTeletravail politique) {
        return ResponseEntity.ok(politiqueService.modifier(id, politique));
    }

    @GetMapping("/{id}/valider")
    public ResponseEntity<Boolean> valider(@PathVariable Long id) {
        return ResponseEntity.ok(politiqueService.valider(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        politiqueService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}