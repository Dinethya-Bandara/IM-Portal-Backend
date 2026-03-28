package com.backend.importal.dto;

import com.backend.importal.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateDTO {
    public String firstName;
    public String lastName;
    public String role;
    public String studentNumber;
    public String universityEmail;
    public String personalEmail;
    public String contactNumber;
    public String batch;
    public String level;
    public MultipartFile studentIdImage;
}
