package com.backend.importal.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exam_preference")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExamPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long preferenceId;

    @Column(nullable = false)
    private String weekendAvailability;

    @Column(nullable = false)
    private Integer dayGaps;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
}
