package com.backend.importal.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "directories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Directory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String position;

    private String phone;   // optional

    private String room;    // optional (only for staff)

    @Column(nullable = false)
    private String type; // "staff" or "student"

    @Column(name = "added_by")
    private String addedBy;
}