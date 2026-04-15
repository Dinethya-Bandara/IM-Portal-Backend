package com.backend.importal.repository;

import com.backend.importal.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {

    List<Calendar> findByCalendarType(String calendarType);

    List<Calendar> findByDateAndCalendarType(LocalDate date, String calendarType);
}