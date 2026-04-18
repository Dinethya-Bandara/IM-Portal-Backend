package com.backend.importal.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ExamPreferenceResponseDTO {

    public String studentEmail;
    public String batch;
    public int level;

    public List<ModuleItem> modules;

    @Data
    @Builder
    public static class ModuleItem {
        public String courseCode;
        public int priority;
    }
}