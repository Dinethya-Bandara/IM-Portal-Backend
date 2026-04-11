package com.backend.importal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimetableRequestDTO {
    public Long batchId;
    public String day;
    public String timeSlot;
    public String courseCode;
    public String venue;
    public String lectureType;
    public String timetableType;
    public String stream;
    public Boolean isCompulsory;
    public Boolean isLunchBreak;
    public LocalDate date;
}
