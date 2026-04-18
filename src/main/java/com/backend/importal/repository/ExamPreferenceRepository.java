package com.backend.importal.repository;

import com.backend.importal.entity.ExamPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamPreferenceRepository extends JpaRepository<ExamPreference, Long> {

    boolean existsByStudentEmail(String studentEmail);

    List<ExamPreference> findByLevel(int level);
}