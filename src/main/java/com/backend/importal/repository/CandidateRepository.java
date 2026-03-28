package com.backend.importal.repository;

import com.backend.importal.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    List<Candidate> findByStatusAndAccountCreated(String status, boolean accountCreated);

    List<Candidate> findByStatus(String status);
}