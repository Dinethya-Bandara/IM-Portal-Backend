package com.backend.importal.controller;

import com.backend.importal.dto.NotificationRequestDTO;
import com.backend.importal.dto.NotificationResponseDTO;
import com.backend.importal.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@CrossOrigin
public class NotificationController {

    private final NotificationService notificationService;

    //Create a new notification
    @PostMapping
    public void createNotification(@RequestBody NotificationRequestDTO dto) {
        notificationService.createNotification(dto);
    }

    //Get notifications for a specific user
    @GetMapping("/{email}")
    public List<NotificationResponseDTO> getUserNotifications(@PathVariable String email) {
        return notificationService.getUserNotifications(email);
    }

    //Mark a single notification as read
    @PutMapping("/read/{notificationId}/{email}")
    public void markAsRead(@PathVariable Long notificationId,
                           @PathVariable String email) {
        notificationService.markAsRead(notificationId, email);
    }

    //Mark all notifications as read for a user
    @PutMapping("/read-all/{email}")
    public void markAllAsRead(@PathVariable String email) {
        notificationService.markAllAsRead(email);
    }

    //Delete a specific notification for a user
    @DeleteMapping("/{notificationId}/{email}")
    public void deleteNotification(@PathVariable Long notificationId,
                                   @PathVariable String email) {
        notificationService.deleteNotification(notificationId, email);
    }
}