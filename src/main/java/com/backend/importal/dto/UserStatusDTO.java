package com.backend.importal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserStatusDTO {
    public Long activeStudents;
    public Long inactiveStudents;
}
