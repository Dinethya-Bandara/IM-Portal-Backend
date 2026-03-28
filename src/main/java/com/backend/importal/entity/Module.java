package com.backend.importal.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "modules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String courseCode;
    private String moduleName;
    private String lecturerName;
}