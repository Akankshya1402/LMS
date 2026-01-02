package com.lms.notification.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationResponse {
    private String id;
    private String recipient;
    private String message;
    private String status;
    private LocalDateTime createdAt;
}
