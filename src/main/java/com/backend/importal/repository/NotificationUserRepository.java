package com.backend.importal.repository;

import com.backend.importal.entity.NotificationUser;
import com.backend.importal.entity.User;
import com.backend.importal.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationUserRepository extends JpaRepository<NotificationUser, Long> {

    //Get all notifications for a user
    List<NotificationUser> findByUserAndDeletedFalse(User user);

    //Get unread notifications
    List<NotificationUser> findByUserAndStatusAndDeletedFalse(User user, Status status);

    //Find specific notification for a user
    Optional<NotificationUser> findByNotification_IdAndUser(Long notificationId, User user);

    //Count of unread notifications
    long countByUserAndStatusAndDeletedFalse(User user, Status status);
}