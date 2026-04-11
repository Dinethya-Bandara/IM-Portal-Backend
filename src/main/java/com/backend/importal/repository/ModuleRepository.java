package com.backend.importal.repository;

import com.backend.importal.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {

    Optional<Module> findByCourseCode(String courseCode);
    Optional<Module> findByModuleName(String moduleName);
}