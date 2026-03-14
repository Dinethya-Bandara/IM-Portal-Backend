package com.backend.importal.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class LoginResponseDTO {
    public boolean isSuccess;
    public String name;
    public String message;
    public String batch;
    public String roleName;
}
