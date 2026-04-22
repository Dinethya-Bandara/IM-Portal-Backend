package com.backend.importal.service;

import com.backend.importal.entity.Module;
import com.backend.importal.repository.ModuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModuleService {

    private final ModuleRepository repository;

    // Search modules by a keyword (course code, name, etc.)
    public List<Module> searchModules(String query) {
        return repository.searchModules(query);
    }

    // Get all modules from the database
    public List<Module> getAllModules() {
        return repository.findAll();
    }
}