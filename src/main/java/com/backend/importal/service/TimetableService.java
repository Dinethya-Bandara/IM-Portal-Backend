package com.backend.importal.service;

import com.backend.importal.dto.TimetableRequestDTO;
import com.backend.importal.dto.TimetableResponseDTO;
import com.backend.importal.entity.*;
import com.backend.importal.entity.Module;
import com.backend.importal.repository.BatchRepository;
import com.backend.importal.repository.ModuleRepository;
import com.backend.importal.repository.TimetableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.backend.importal.entity.LectureType;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
    public class TimetableService {

        @Autowired
        private BatchRepository batchRepository;

        @Autowired
        private ModuleRepository moduleRepository;

        @Autowired
        private TimetableRepository timetableRepository;

        @Autowired
        private ErrorLogService errorLogService;

        //Add a new timetable entry (Academic or Exam)
        public TimetableResponseDTO addTimetable(TimetableRequestDTO request) {

            // Get batch from DB
            Batch batch = batchRepository.findById(request.getBatchId())
                    .orElseThrow(() -> new RuntimeException("Batch not found"));

            Module module = null;

            // If not a lunch break, module must exist
            if (!Boolean.TRUE.equals(request.getIsLunchBreak())) {

                if (request.getCourseCode() == null || request.getCourseCode().isBlank()) {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Course code is required"
                    );
                }

                // Find module by course code
                module = moduleRepository.findByCourseCode(request.getCourseCode().trim())
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST, "Module not found"
                        ));
            }

            // Convert timetable type string → ENUM
            TimetableType timetableType;

            try {
                timetableType = TimetableType.valueOf(request.getTimetableType().toUpperCase());
            } catch (Exception e) {
                errorLogService.logError(e, "TimetableService", "addTimetable");
                throw new RuntimeException("Invalid timetable type");
            }

            LectureType lectureType = null;

            // Check if venue already booked
            if (timetableType == TimetableType.ACADEMIC && !Boolean.TRUE.equals(request.getIsLunchBreak())) {

                if (request.getLectureType() == null) {
                    throw new RuntimeException("Lecture type is required for academic timetable");
                }

                try {
                    lectureType = LectureType.valueOf(request.getLectureType().toUpperCase());
                } catch (Exception e) {
                    errorLogService.logError(e, "TimetableService", "addTimetable");
                    throw new RuntimeException("Invalid lecture type");
                }
            }

            boolean isBooked;

            if (timetableType == TimetableType.ACADEMIC) {

                // Academic validation (by DAY)
                isBooked = timetableRepository.existsByDayAndTimeSlotAndVenue(
                        request.getDay(),
                        request.getTimeSlot(),
                        request.getVenue()
                );

            } else {

                // Exam validation (by DATE)
                isBooked = timetableRepository.existsByDateAndTimeSlotAndVenueAndTimetableType(
                        request.getDate(),
                        request.getTimeSlot(),
                        request.getVenue(),
                        TimetableType.EXAM
                );
            }

            if (isBooked) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This hall is already booked");
            }

            String day;

            // Determine day based on timetable type
            if (timetableType == TimetableType.EXAM) {

                if (request.getDate() == null) {
                    throw new RuntimeException("Date is required for EXAM");
                }

                day = request.getDate().getDayOfWeek().toString();

            } else {

                if (request.getDay() == null) {
                    throw new RuntimeException("Day is required for ACADEMIC");
                }

                day = request.getDay();
            }

            // Create timetable entity using builder pattern
            Timetable timetable = Timetable.builder()
                    .batch(batch)
                    .module(module)
                    .day(day)
                    .date(timetableType == TimetableType.EXAM ? request.getDate() : null)
                    .timeSlot(request.getTimeSlot())
                    .venue(request.getVenue())
                    .lectureType(lectureType)
                    .timetableType(timetableType)
                    .stream(timetableType == TimetableType.ACADEMIC ? request.getStream() : null)
                    .isCompulsory(timetableType == TimetableType.ACADEMIC ? request.getIsCompulsory() : null)
                    .isLunchBreak(timetableType == TimetableType.ACADEMIC ? request.getIsLunchBreak() : null)
                    .createdAt(LocalDateTime.now())
                    .build();

            // Save to database
            Timetable saved = timetableRepository.save(timetable);

            // Convert entity → DTO
            return mapToResponse(saved);
        }

        //Convert Timetable entity to response DTO
        private TimetableResponseDTO mapToResponse(Timetable t) {
            return TimetableResponseDTO.builder()
                    .id(t.getId())
                    .batch(t.getBatch().getBatch())
                    .day(t.getDay())
                    .timeSlot(t.getTimeSlot())
                    .venue(t.getVenue())
                    .date(t.getDate())
                    .type(t.getTimetableType().name())
                    .stream(t.getStream())
                    .isCompulsory(t.getIsCompulsory())
                    .isLunchBreak(t.getIsLunchBreak())
                    .moduleId(t.getModule() != null ? t.getModule().getId() : null)
                    .courseCode(t.getModule() != null ? t.getModule().getCourseCode() : null)
                    .moduleName(t.getModule() != null ? t.getModule().getModuleName() : "Lunch Break")
                    .lecturerName(t.getModule() != null ? t.getModule().getLecturerName() : null)
                    .lectureType(t.getLectureType() != null ? t.getLectureType().name() : null)
                    .build();
        }

        //Delete timetable entry by ID
        public void deleteTimetable(Long id) {
            timetableRepository.deleteById(id);
        }

        //Get timetable by type (ACADEMIC / EXAM)
        public List<TimetableResponseDTO> getByType(String type) {

            TimetableType timetableType = TimetableType.valueOf(type.toUpperCase());

            return timetableRepository.findByTimetableType(timetableType)
                    .stream()
                    .map(this::mapToResponse)
                    .toList();
        }

        //Get timetable by batch and type
        public List<TimetableResponseDTO> getByBatchAndType(Long batchId, String type) {

            TimetableType timetableType;

            try {
                timetableType = TimetableType.valueOf(type.toUpperCase());
            } catch (Exception e) {
                errorLogService.logError(e, "TimetableService", "getByBatchAndType");
                throw new RuntimeException("Invalid timetable type");
            }

            Batch batch = batchRepository.findById(batchId)
                    .orElseThrow(() -> new RuntimeException("Batch not found"));

            return timetableRepository
                    .findByBatchAndTimetableType(batch, timetableType)
                    .stream()
                    .map(this::mapToResponse)
                    .toList();
        }
    }


