package com.backend.importal.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    private LocalDateTime createdAt;

    // IMPORTANT → to support "My Submissions"
    private String studentUsername; // NOT shown to lecturers

    private boolean read;
}