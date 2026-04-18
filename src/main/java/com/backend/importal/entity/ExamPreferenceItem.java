package com.backend.importal.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamPreferenceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_code")
    private String courseCode;
    private int priority;

    @ManyToOne
    @JoinColumn(name = "exam_preference_id")
    private ExamPreference examPreference;
}