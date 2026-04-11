package com.backend.importal.repository;

import com.backend.importal.entity.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BatchRepository extends JpaRepository<Batch, Long> {
    Optional<Batch> findByBatch(String batch);
}