package com.backend.importal.dto;

import lombok.Data;
import java.util.List;

@Data
public class ExamPreferenceRequestDTO {

    public String studentEmail;
    public String batch;
    public int level;
    public String gap;
    public boolean satAvailable;
    public boolean sunAvailable;

    public List<ModuleItem> modules;

    @Data
    public static class ModuleItem {
        public String courseCode;
        public int priority;
    }
}