package com.lms.notification.consumer;

import com.lms.notification.config.KafkaTopics;
import com.lms.notification.dto.NotificationEvent;
import com.lms.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventConsumer {

    private final NotificationService service;

    @KafkaListener(
        topics = {
            KafkaTopics.LOAN_APPROVED,
            KafkaTopics.EMI_DUE,
            KafkaTopics.LOAN_CLOSED
        },
        groupId = "notification-group"
    )
    public void consume(NotificationEvent event) {
        service.send(event);
    }
}
