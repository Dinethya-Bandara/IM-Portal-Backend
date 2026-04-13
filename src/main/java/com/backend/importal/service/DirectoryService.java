package com.backend.importal.service;

import com.backend.importal.entity.Directory;
import com.backend.importal.repository.DirectoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectoryService {

    private final DirectoryRepository repository;

    public Directory addEntry(Directory directory) {

        // validation
        if (directory.getName() == null || directory.getName().isEmpty())
            throw new RuntimeException("Name is required");

        if (directory.getEmail() == null || directory.getEmail().isEmpty())
            throw new RuntimeException("Email is required");

        if (directory.getPosition() == null || directory.getPosition().isEmpty())
            throw new RuntimeException("Position is required");

        if (directory.getType() == null ||
                (!directory.getType().equals("staff") && !directory.getType().equals("student"))) {
            throw new RuntimeException("Type must be 'staff' or 'student'");
        }

        if (directory.getPhone() != null && !directory.getPhone().isEmpty()) {
            if (!directory.getPhone().matches("\\d{10}")) {
                throw new RuntimeException("Phone number must be exactly 10 digits");
            }
        }

        return repository.save(directory);
    }

    public List<Directory> getByType(String type) {
        return repository.findByType(type);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}