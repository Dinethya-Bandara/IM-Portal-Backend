package com.backend.importal.controller;

import com.backend.importal.dto.ExamPreferenceRequestDTO;
import com.backend.importal.dto.ExamPreferenceRequestDTO;
import com.backend.importal.dto.ExamPreferenceResponseDTO;
import com.backend.importal.service.ExamPreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/exam-preferences")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class ExamPreferenceController {

    private final ExamPreferenceService service;

    @PostMapping
    public void save(@RequestBody ExamPreferenceRequestDTO request) {
        service.savePreference(request);
    }

    @GetMapping("/check")
    public Map<String, Boolean> checkSubmission(@RequestParam String email) {
        boolean exists = service.hasSubmitted(email);
        return Map.of("submitted", exists);
    }

    @GetMapping
    public List<ExamPreferenceResponseDTO> getPreferences(
            @RequestParam int level,
            @RequestParam int semester
    ) {
        return service.getPreferences(level, semester);
    }
}