package com.backend.importal.service;

import com.backend.importal.dto.ExamPreferenceRequestDTO;
import com.backend.importal.dto.ExamPreferenceResponseDTO;
import com.backend.importal.entity.ExamPreference;
import com.backend.importal.entity.ExamPreferenceItem;
import com.backend.importal.repository.ExamPreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamPreferenceService {

    private final ExamPreferenceRepository repository;

    //Save student exam preferences
    public void savePreference(ExamPreferenceRequestDTO request) {

        // Check if student already submitted preferences
        if (repository.existsByStudentEmail(request.getStudentEmail())) {
            throw new RuntimeException("You have already submitted preferences");
        }

        // Create main preference object
        ExamPreference pref = ExamPreference.builder()
                .studentEmail(request.getStudentEmail())
                .batch(request.getBatch())
                .level(request.getLevel())
                .gap(request.getGap())
                .satAvailable(request.isSatAvailable())
                .sunAvailable(request.isSunAvailable())
                .build();

        // Convert module list from request → entity list
        pref.setModules(
                request.getModules().stream().map(m ->
                        ExamPreferenceItem.builder()
                                .courseCode(m.getCourseCode())
                                .priority(m.getPriority())
                                .examPreference(pref)
                                .build()
                ).collect(Collectors.toList())
        );

        repository.save(pref);
    }

    //Check if student already submitted preferences
    public boolean hasSubmitted(String email) {
        return repository.existsByStudentEmail(email);
    }

    //Get preferences filtered by level and semester
    public List<ExamPreferenceResponseDTO> getPreferences(int level, int semester) {

        // Get all preferences for given level
        List<ExamPreference> prefs = repository.findByLevel(level);

        // Convert to response DTO
        return prefs.stream()
                .map(pref -> {

                    // Filter modules by level & semester
                    List<ExamPreferenceResponseDTO.ModuleItem> filteredModules =
                            pref.getModules().stream()
                                    .filter(m -> {
                                        // Extract digits from course code
                                        String code = m.getCourseCode();

                                        if (code == null || code.isBlank()) {
                                            return false; // skip invalid records
                                        }

                                        String digits = code.replaceAll("\\D", ""); // Remove letters → keep only numbers

                                        if (digits.length() < 2) {
                                            return false; // invalid format
                                        }

                                        // First digit → level, second → semester
                                        int moduleLevel = Character.getNumericValue(digits.charAt(0));
                                        int moduleSemester = Character.getNumericValue(digits.charAt(1));

                                        // Only keep matching modules
                                        return moduleLevel == level && moduleSemester == semester;
                                    })
                                    .map(m -> ExamPreferenceResponseDTO.ModuleItem.builder()
                                            .courseCode(m.getCourseCode())
                                            .priority(m.getPriority())
                                            .build()
                                    )
                                    .toList();

                    // Build response object
                    return ExamPreferenceResponseDTO.builder()
                            .studentEmail(pref.getStudentEmail())
                            .batch(pref.getBatch())
                            .level(pref.getLevel())
                            .modules(filteredModules)
                            .build();
                })
                .toList();
    }
}