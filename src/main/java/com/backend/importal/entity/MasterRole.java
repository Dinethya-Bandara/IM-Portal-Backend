package com.backend.importal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "master_roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MasterRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_name", unique = true)
    private String roleName;

    @JsonIgnore
    @OneToMany(mappedBy = "masterRole", fetch = FetchType.LAZY)
    private List<Role> role = new ArrayList<>();
}