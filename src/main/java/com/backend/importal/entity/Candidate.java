package com.backend.importal.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "candidates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_Id", nullable = true)
    private Role role;

    @Column(name = "student_number")
    private String studentNumber;

    @Column(name = "university_email")
    private String universityEmail;

    @Column(name = "personal_email")
    private String personalEmail;

    @Column(name = "contact_number")
    private String contactNumber;

    private String batch;
    private String level;

    @Column(name = "student_id_file")
    private String studentIdUrl;

    @Column(nullable = false)
    private String status = "PENDING";

    private Boolean accountCreated = false;

    @Transient
    private MultipartFile studentIdImage;
}