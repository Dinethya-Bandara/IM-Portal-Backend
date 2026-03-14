package com.backend.importal.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "modules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long moduleId;

    @Column(nullable = false)
    private String moduleName;

    @Column(nullable = false)
    private Integer credits;

    @Column(nullable = false)
    private String assignedBatch;
}
