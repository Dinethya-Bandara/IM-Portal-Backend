package com.backend.importal.service;

import com.backend.importal.dto.NotificationRequestDTO;
import com.backend.importal.dto.NotificationResponseDTO;
import com.backend.importal.entity.*;
import com.backend.importal.repository.NotificationRepository;
import com.backend.importal.repository.NotificationUserRepository;
import com.backend.importal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.backend.importal.entity.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationUserRepository notificationUserRepository;
    private final UserRepository userRepository;

    //CREATE NOTIFICATION
    public void createNotification(NotificationRequestDTO dto) {

        //Get sender from DB
        User sender = userRepository.findByPersonalEmail(dto.getSenderEmail())
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        if (sender == null) {
            throw new RuntimeException("Sender not found");
        }

        //Create Notification
        Notification notification = new Notification();
        notification.setTitle(dto.getTitle());
        notification.setMessage(dto.getMessage());
        notification.setSender(sender);
        notification.setTargetAudience(TargetAudience.valueOf(dto.getTargetAudience()));
        notification.setPriority(Priority.valueOf(dto.getPriority()));
        notification.setCreatedAt(LocalDateTime.now());

        Notification savedNotification = notificationRepository.save(notification);

        //Find target users
        Set<User> targetUsers = new HashSet<>();

        if (dto.getTargetAudience().equals("ALL")) {
            targetUsers.addAll(userRepository.findAll());
        } else {
            String level = dto.getTargetAudience().replace("LEVEL_", "Level ");
            targetUsers.addAll(userRepository.findByLevel(level));
        }

        // include sender ALWAYS
        targetUsers.add(sender);

        // include admins (no duplicates now)
        //targetUsers.addAll(userRepository.findByRole_RoleNameIgnoreCase("admin"));
        List<NotificationUser> notificationUsers = new ArrayList<>();

        for (User user : targetUsers) {

            // prevent duplicate insertion
            boolean exists = notificationUserRepository
                    .findByNotification_IdAndUser(savedNotification.getId(), user)
                    .isPresent();

            if (exists) continue;

            NotificationUser nu = new NotificationUser();
            nu.setNotification(savedNotification);
            nu.setUser(user);
            nu.setStatus(Status.UNREAD);
            nu.setDeleted(false);

            notificationUsers.add(nu);
        }

        notificationUserRepository.saveAll(notificationUsers);
    }

    //GET USER NOTIFICATIONS
    public List<NotificationResponseDTO> getUserNotifications(String email) {

        User user = userRepository.findByPersonalEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<NotificationUser> list =
                notificationUserRepository.findByUserAndDeletedFalse(user);

        Set<Long> seen = new HashSet<>();

        List<NotificationResponseDTO> responseList = new ArrayList<>();

        for (NotificationUser nu : list) {

            Notification n = nu.getNotification();

            if (seen.contains(n.getId())) continue;
            seen.add(n.getId());

            NotificationResponseDTO dto = new NotificationResponseDTO(
                    n.getId(),
                    n.getTitle(),
                    n.getMessage(),
                    n.getSender().getName(),
                    n.getTargetAudience().name(),
                    n.getPriority().name(),
                    n.getCreatedAt(),
                    nu.getStatus().name()
            );

            responseList.add(dto);
        }

        return responseList;
    }

    //MARK ONE AS READ
    public void markAsRead(Long notificationId, String email) {

        User user = userRepository.findByPersonalEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        NotificationUser nu =
                notificationUserRepository
                        .findByNotification_IdAndUser(notificationId, user)
                        .orElse(null);

                        if (nu == null) return;

        nu.setStatus(Status.READ);

        notificationUserRepository.save(nu);
    }

    //MARK ALL AS READ
    public void markAllAsRead(String email) {

        User user = userRepository.findByPersonalEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<NotificationUser> list =
                notificationUserRepository.findByUserAndDeletedFalse(user);

        for (NotificationUser nu : list) {
            nu.setStatus(Status.READ);
        }

        notificationUserRepository.saveAll(list);
    }

    //DELETE
    public void deleteNotification(Long notificationId, String email) {

        User user = userRepository.findByPersonalEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        NotificationUser nu =
                notificationUserRepository
                        .findByNotification_IdAndUser(notificationId, user)
                        .orElseThrow(() -> new RuntimeException("Notification not found"));

        nu.setDeleted(true);

        notificationUserRepository.save(nu);
    }
}