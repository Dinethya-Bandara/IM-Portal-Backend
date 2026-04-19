package com.backend.importal.service;

import com.backend.importal.dto.ProfileResponseDTO;
import com.backend.importal.dto.ProfileUpdateDTO;
import com.backend.importal.entity.User;
import com.backend.importal.repository.UserRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;

    @Autowired
    private Cloudinary cloudinary;

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

    //UPDATE PROFILE
    public void updateProfile(String personalEmail, ProfileUpdateDTO dto) throws IOException {

        User user = userRepository.findByPersonalEmail(personalEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // update only allowed fields
        user.setName(dto.getFullName());

        if (dto.getProfileImage() != null) {
            Map uploadResult = cloudinary.uploader().upload(dto.getProfileImage().getBytes(), ObjectUtils.asMap(
                    "folder", "profile_pictures"
            ));
            user.setProfileImage((String) uploadResult.get("secure_url"));
        }

        userRepository.save(user);
    }
}
