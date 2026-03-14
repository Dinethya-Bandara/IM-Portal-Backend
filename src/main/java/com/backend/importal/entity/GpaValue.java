package com.backend.importal.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "gpa_values")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GpaValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gpaId;

    @Column(nullable = false, unique = true)
    private String grade;   // e.g., A+, A, B+

    @Column(nullable = false)
    private Double value;   // e.g., 4.0, 3.7
}
