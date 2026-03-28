package com.backend.importal.repository;

import com.backend.importal.entity.Otp;
import com.backend.importal.entity.User;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    List<Otp> findByEmailAndExpired(String email, boolean expired);
    Optional<Otp> findTopByEmailOrderByGeneratedTimeDesc(String email);
}
