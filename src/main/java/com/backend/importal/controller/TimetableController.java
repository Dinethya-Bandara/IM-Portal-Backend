package com.backend.importal.controller;

import com.backend.importal.dto.TimetableRequestDTO;
import com.backend.importal.dto.TimetableResponseDTO;
import com.backend.importal.service.TimetableService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/timetable")
@CrossOrigin
public class TimetableController {

    @Autowired
    private TimetableService timetableService;

    @PostMapping
    public ResponseEntity<?> addTimetable(@Valid @RequestBody TimetableRequestDTO request) {
        try {
            TimetableResponseDTO response = timetableService.addTimetable(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (ResponseStatusException ex) {
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .body(ex.getReason());
        }
    }

    @DeleteMapping("/{id}")
    public String deleteTimetable(@PathVariable Long id) {
        timetableService.deleteTimetable(id);
        return "Deleted successfully";
    }

    @GetMapping
    public List<TimetableResponseDTO> getTimetable(
            @RequestParam Long batchId,
            @RequestParam String type
    ) {
        return timetableService.getByBatchAndType(batchId, type);
    }
}