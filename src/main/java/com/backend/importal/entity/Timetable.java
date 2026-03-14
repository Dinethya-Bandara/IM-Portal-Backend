package com.backend.importal.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "timetable")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Timetable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long timetableId;

    @Column(nullable = false)
    private LocalDate datePosted;

    @Column(nullable = false)
    private String level;

    @Column(nullable = false)
    private String type;
}
