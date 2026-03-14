package com.backend.importal.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "announcements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long announcementId;

    @Column(nullable = false)
    private LocalDate datePosted;

    @Column(nullable = false)
    private String targetBatch;

    @Column(nullable = false, length = 2000)
    private String message;
}
