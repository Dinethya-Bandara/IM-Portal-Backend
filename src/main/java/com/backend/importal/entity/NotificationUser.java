package com.backend.importal.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notification_user", uniqueConstraints = @UniqueConstraint(columnNames = {"notification_id", "user_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "notification_id", nullable = false)
    private Notification notification;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private Status status;

    private boolean deleted = false;
}