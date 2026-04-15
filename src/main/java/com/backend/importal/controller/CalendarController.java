package com.backend.importal.controller;

import com.backend.importal.entity.Calendar;
import com.backend.importal.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calendar")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService service;

    @PostMapping
    public Calendar saveEvent(@RequestBody Calendar event) {
        return service.saveEvent(event);
    }

    @GetMapping("/type/{type}")
    public List<Calendar> getByType(@PathVariable String type) {
        return service.getByType(type);
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Long id) {
        service.deleteEvent(id);
    }
}