package com.backend.importal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "otp")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long otpId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String otpCode;

    @Column(nullable = false, updatable = false)
    private LocalDateTime generatedTime;

    @Column(nullable = false)
    private boolean expired;

    @PrePersist
    public void prePersist() {
        generatedTime = LocalDateTime.now();
        expired = false;
    }
}