package com.backend.importal.repository;

import com.backend.importal.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {

    Optional<Module> findByCourseCode(String courseCode);
    Optional<Module> findByModuleName(String moduleName);

    @Query("SELECT m FROM Module m WHERE LOWER(m.courseCode) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(m.moduleName) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Module> searchModules(@Param("query") String query);

}