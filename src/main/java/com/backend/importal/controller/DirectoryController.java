package com.backend.importal.controller;

import com.backend.importal.entity.Directory;
import com.backend.importal.service.DirectoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/directories")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class DirectoryController {

    private final DirectoryService service;

    @PostMapping
    public Directory addEntry(@RequestBody Directory directory) {
        return service.addEntry(directory);
    }

    @GetMapping("/{type}")
    public List<Directory> getByType(@PathVariable String type) {
        return service.getByType(type);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}