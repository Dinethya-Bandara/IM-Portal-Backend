package com.backend.importal.service;

import com.backend.importal.entity.Calendar;
import com.backend.importal.repository.CalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository repository;

    public Calendar saveEvent(Calendar event) {
        return repository.save(event);
    }

    public List<Calendar> getAllEvents() {
        return repository.findAll();
    }

    public List<Calendar> getByType(String type) {
        return repository.findByCalendarType(type);
    }

    public void deleteEvent(Long id) {
        repository.deleteById(id);
    }
}