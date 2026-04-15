package com.backend.importal.repository;

import com.backend.importal.entity.FeedbackReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface FeedbackReadStatusRepository extends JpaRepository<FeedbackReadStatus, Long> {

    Optional<FeedbackReadStatus> findByFeedbackIdAndUsername(Long feedbackId, String username);

    List<FeedbackReadStatus> findByUsername(String username);
}