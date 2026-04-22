package com.backend.importal.service;

import com.backend.importal.entity.Feedback;
import com.backend.importal.entity.FeedbackReadStatus;
import com.backend.importal.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.backend.importal.repository.FeedbackReadStatusRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository repository;

    private final FeedbackReadStatusRepository readRepo;

    //Submit new feedback
    public Feedback submitFeedback(String message, String username) {
        Feedback feedback = Feedback.builder()
                .message(message)
                .studentUsername(username)
                .createdAt(LocalDateTime.now())
                .build();

        // Save feedback to database
        return repository.save(feedback);
    }

    //Get all feedbacks (for admin/lecturer)
    public List<Feedback> getAllFeedbacks() {
        return repository.findAll();
    }

    public List<Feedback> getMyFeedbacks(String username) {
        return repository.findByStudentUsername(username);
    }

    public void deleteFeedback(Long id) {
        repository.deleteById(id);
    }

    public Feedback getById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public Feedback save(Feedback feedback) {
        return repository.save(feedback);
    }

    // Get all feedbacks with read/unread status for a specific user
    public List<Map<String, Object>> getAllFeedbacksWithReadStatus(String username) {

        List<Feedback> feedbacks = repository.findAll();

        return feedbacks.stream().map(fb -> {
            // Check if this feedback is read by this user
            boolean isRead = readRepo
                    .findByFeedbackIdAndUsername(fb.getId(), username)
                    .map(FeedbackReadStatus::isRead)
                    .orElse(false);

            // Create a response object (Map)
            Map<String, Object> map = new HashMap<>();
            map.put("id", fb.getId());
            map.put("message", fb.getMessage());
            map.put("createdAt", fb.getCreatedAt());
            map.put("read", isRead);

            return map;
        }).toList();
    }

    //Mark a feedback as read for a specific user
    public void markAsRead(Long feedbackId, String username) {

        // Check if a record already exists
        FeedbackReadStatus status = readRepo
                .findByFeedbackIdAndUsername(feedbackId, username)
                .orElse(null);

        // If not exist, create new record
        if (status == null) {
            status = new FeedbackReadStatus();
            status.setFeedbackId(feedbackId);
            status.setUsername(username);
        }

        status.setRead(true); // Mark as read
        readRepo.save(status); // Save to database
    }
}