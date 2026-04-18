package com.backend.importal.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentEmail;
    private String batch;
    private int level;

    private String gap;

    private boolean satAvailable;
    private boolean sunAvailable;

    @OneToMany(mappedBy = "examPreference", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ExamPreferenceItem> modules;
}