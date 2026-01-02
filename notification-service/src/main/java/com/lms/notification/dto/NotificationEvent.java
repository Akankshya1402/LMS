package com.lms.notification.dto;

import lombok.Data;

@Data
public class NotificationEvent {
    private String referenceId;
    private String recipient;
    private String message;
}
