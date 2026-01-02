package com.lms.notification.model;

import com.lms.notification.model.enums.NotificationChannel;
import com.lms.notification.model.enums.NotificationStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "notifications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    private String id;

    private String referenceId; // loanId / applicationId
    private String recipient;
    private String message;

    private NotificationChannel channel;
    private NotificationStatus status;

    private LocalDateTime createdAt;
}
