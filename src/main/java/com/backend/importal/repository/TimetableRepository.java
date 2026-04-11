package com.backend.importal.repository;

import com.backend.importal.entity.Batch;
import com.backend.importal.entity.Timetable;
import com.backend.importal.entity.TimetableType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TimetableRepository extends JpaRepository<Timetable, Long> {

    @Query("SELECT t FROM Timetable t JOIN FETCH t.module WHERE t.batch.batch = :batch AND t.timetableType = :type")
    List<Timetable> findByBatchAndType(
            @Param("batch") String batch,
            @Param("type") TimetableType type
    );

    boolean existsByDayAndTimeSlotAndVenue(
            String day,
            String timeSlot,
            String venue
    );

    boolean existsByDateAndTimeSlotAndVenueAndTimetableType(
            LocalDate date,
            String timeSlot,
            String venue,
            TimetableType timetableType
    );

    List<Timetable> findByTimetableType(TimetableType timetableType);

    List<Timetable> findByBatchAndTimetableType(Batch batch, TimetableType timetableType);

    List<Timetable> findByDayAndTimeSlotAndVenue(String day, String timeSlot, String venue);
}