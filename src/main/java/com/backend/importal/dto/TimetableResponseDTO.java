package com.backend.importal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimetableResponseDTO {
    public Long id;
    public String batch;
    public String day;
    public String timeSlot;

    public String venue;
    public String type;
    public String stream;
    public Boolean isCompulsory;
    public Boolean isLunchBreak;

    public Long moduleId;
    public String courseCode;
    public String moduleName;
    public String lecturerName;
    private String lectureType;

    public LocalDate date;
}
