package com.backend.importal.service;

import com.backend.importal.entity.ErrorLog;
import com.backend.importal.repository.ErrorLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ErrorLogService {

    private final ErrorLogRepository errorLogRepository;

    public void logError(Exception e, String className, String methodName) {

        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));

        ErrorLog errorLog = ErrorLog.builder()
                .errorMessage(e.getMessage())
                .stackTrace(sw.toString())
                .className(className)
                .methodName(methodName)
                .timestamp(LocalDateTime.now())
                .build();

        errorLogRepository.save(errorLog);
    }
}

/*
 This method logs errors into the database when an exception occurs. It takes the error, class name,
 and method name, converts the full stack trace into a string, and stores it along with the error
 message and timestamp. This helps in tracking and debugging issues later instead of only using console logs.
*/