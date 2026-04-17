package com.backend.importal.controller;

import com.backend.importal.entity.Module;
import com.backend.importal.service.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modules")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleService service;

    @GetMapping("/search")
    public List<Module> search(@RequestParam String query) {
        return service.searchModules(query);
    }
}