package com.backend.importal.service;

import com.backend.importal.dto.ProfileResponseDTO;
import com.backend.importal.dto.ProfileUpdateDTO;
import com.backend.importal.entity.User;
import com.backend.importal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;

    // 🔹 GET PROFILE
    public ProfileResponseDTO getProfile(String personalEmail) {

        User user = userRepository.findByPersonalEmail(personalEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ProfileResponseDTO.builder()
                .fullName(user.getName())
                .email(user.getPersonalEmail()) // map to frontend "email"
                .batch(user.getBatch())
                .role(user.getRole().getRoleName()) // from role table
                .profileImage(user.getProfileImage())
                .build();
    }

    // 🔹 UPDATE PROFILE
    public void updateProfile(String personalEmail, ProfileUpdateDTO dto) {

        User user = userRepository.findByPersonalEmail(personalEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // update only allowed fields
        user.setName(dto.getFullName());

        if (dto.getProfileImage() != null && !dto.getProfileImage().isEmpty()) {
            user.setProfileImage(dto.getProfileImage());
        }

        userRepository.save(user);
    }
}
