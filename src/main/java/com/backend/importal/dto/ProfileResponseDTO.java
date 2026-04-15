package com.backend.importal.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProfileResponseDTO {
    public String fullName;
    public String email;
    public String batch;
    public String role;
    public String profileImage;
}
