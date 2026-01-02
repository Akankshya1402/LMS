package com.lms.notification.controller;

import com.lms.notification.dto.NotificationResponse;
import com.lms.notification.exception.NotificationNotFoundException;
import com.lms.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository repository;

    @GetMapping("/{id}")
    public NotificationResponse getById(@PathVariable String id) {
        var notification = repository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found"));

        return NotificationResponse.builder()
                .id(notification.getId())
                .recipient(notification.getRecipient())
                .message(notification.getMessage())
                .status(notification.getStatus().name())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}

