package com.backend.importal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDTO {

    public String title;
    public String message;
    public String targetAudience;
    public String priority;
    public String senderEmail;
}