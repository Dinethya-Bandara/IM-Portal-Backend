package com.backend.importal.dto;

import lombok.Data;

@Data
public class ResetPasswordRequestDTO {
    public String email;
    public String newPassword;

}
