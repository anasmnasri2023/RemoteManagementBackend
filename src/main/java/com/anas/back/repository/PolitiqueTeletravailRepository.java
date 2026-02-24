package com.anas.back.repository;

import com.anas.back.model.PolitiqueTeletravail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PolitiqueTeletravailRepository extends JpaRepository<PolitiqueTeletravail, Long> {
    List<PolitiqueTeletravail> findByRhId(Long rhId);
}