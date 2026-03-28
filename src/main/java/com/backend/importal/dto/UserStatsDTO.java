package com.backend.importal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserStatsDTO {
    private long totalUsers;
    private long students;
    private long lecturers;
    private long juniorStaff;
}