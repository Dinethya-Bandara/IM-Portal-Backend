package com.backend.importal.controller;

import com.backend.importal.dto.ProfileResponseDTO;
import com.backend.importal.dto.ProfileUpdateDTO;
import com.backend.importal.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ProfileController {

    private final ProfileService profileService;

    //Get user profile details
    @GetMapping
    public ProfileResponseDTO getProfile(@RequestParam String personalEmail) {
        return profileService.getProfile(personalEmail);
    }

    //Update user profile details
    @PutMapping
    public void updateProfile(@RequestParam String personalEmail,
                              @RequestBody ProfileUpdateDTO dto) throws IOException {
        profileService.updateProfile(personalEmail, dto);
    }
}