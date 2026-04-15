package com.backend.importal.controller;

import com.backend.importal.dto.ProfileResponseDTO;
import com.backend.importal.dto.ProfileUpdateDTO;
import com.backend.importal.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ProfileResponseDTO getProfile(@RequestParam String personalEmail) {
        return profileService.getProfile(personalEmail);
    }

    @PutMapping
    public void updateProfile(@RequestParam String personalEmail,
                              @RequestBody ProfileUpdateDTO dto) {
        profileService.updateProfile(personalEmail, dto);
    }
}