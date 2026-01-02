package com.lms.notification.service.impl;

import com.lms.notification.dto.NotificationEvent;
import com.lms.notification.dto.NotificationResponse;
import com.lms.notification.model.Notification;
import com.lms.notification.model.enums.NotificationChannel;
import com.lms.notification.model.enums.NotificationStatus;
import com.lms.notification.repository.NotificationRepository;
import com.lms.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;

    @Override
    public NotificationResponse send(NotificationEvent event) {

        Notification notification = Notification.builder()
                .referenceId(event.getReferenceId())
                .recipient(event.getRecipient())
                .message(event.getMessage())
                .channel(NotificationChannel.EMAIL)
                .status(NotificationStatus.SENT)
                .createdAt(LocalDateTime.now())
                .build();

        Notification saved = repository.save(notification);

        return NotificationResponse.builder()
                .id(saved.getId())
                .recipient(saved.getRecipient())
                .message(saved.getMessage())
                .status(saved.getStatus().name())
                .createdAt(saved.getCreatedAt())
                .build();
    }
}
