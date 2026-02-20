package com.anas.back.repository;

import com.anas.back.model.Rapport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RapportRepository extends JpaRepository<Rapport, Long> {
    List<Rapport> findByRhId(Long rhId);
}