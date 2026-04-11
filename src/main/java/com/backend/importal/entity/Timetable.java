package com.backend.importal.entity;

import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "timetables")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Timetable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String day;
    private String timeSlot;

    @Enumerated(EnumType.STRING)
    @Column(name = "timetable_type")
    private TimetableType timetableType;

    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module module;

    @Enumerated(EnumType.STRING)
    private LectureType lectureType;

    private String stream;
    private Boolean isCompulsory;
    private String venue;

    private LocalDate date;

    private Boolean isLunchBreak;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "batch_id")
    private Batch batch;
}