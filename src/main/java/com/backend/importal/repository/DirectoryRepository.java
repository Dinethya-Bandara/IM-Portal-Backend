package com.backend.importal.repository;

import com.backend.importal.entity.Directory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DirectoryRepository extends JpaRepository<Directory, Long> {

    List<Directory> findByType(String type);
}