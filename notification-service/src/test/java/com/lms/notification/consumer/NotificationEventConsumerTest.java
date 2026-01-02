package com.lms.notification.consumer;

import com.lms.notification.dto.NotificationEvent;
import com.lms.notification.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.mockito.Mockito.*;

class NotificationEventConsumerTest {

    @Test
    void shouldConsumeKafkaEvent() {
        NotificationService service = mock(NotificationService.class);
        NotificationEventConsumer consumer = new NotificationEventConsumer(service);

        NotificationEvent event = new NotificationEvent();
        event.setMessage("EMI Due");

        consumer.consume(event);

        verify(service).send(event);
    }
}
