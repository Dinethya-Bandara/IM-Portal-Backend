package com.backend.importal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponseDTO {

    public Long id;
    public String title;
    public String message;
    public String senderName;
    public String targetAudience;
    public String priority;
    public LocalDateTime createdAt;
    public String status;
}