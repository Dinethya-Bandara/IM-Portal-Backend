package com.backend.importal.controller;

import com.backend.importal.entity.Feedback;
import com.backend.importal.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class FeedbackController {

    private final FeedbackService service;

    //Submit (Student)
    @PostMapping
    public Feedback submit(
            @RequestParam String message,
            @RequestParam String username
    ) {
        return service.submitFeedback(message, username);
    }

    //Get feedbacks (WITH per-user read status)
    @GetMapping
    public List<?> getAll(@RequestParam(required = false) String username) {
        if (username != null) {
            return service.getAllFeedbacksWithReadStatus(username);
        } else {
            return service.getAllFeedbacks();
        }
    }

    //Student "My Submissions"
    @GetMapping("/my")
    public List<Feedback> getMine(@RequestParam String username) {
        return service.getMyFeedbacks(username);
    }

    //Delete (both student & lecturer)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteFeedback(id);
    }

    //Mark as read (PER USER)
    @PutMapping("/{id}/read")
    public void markAsRead(
            @PathVariable Long id,
            @RequestParam String username
    ) {
        service.markAsRead(id, username);
    }
}