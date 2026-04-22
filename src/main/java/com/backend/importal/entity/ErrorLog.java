package com.backend.importal.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String errorMessage;

    @Column(length = 5000)
    private String stackTrace;

    @Column
    private String className;

    @Column
    private String methodName;

    @Column
    private LocalDateTime timestamp;
}