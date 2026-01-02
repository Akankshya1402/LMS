package com.lms.notification.service;

import com.lms.notification.dto.NotificationEvent;
import com.lms.notification.model.Notification;
import com.lms.notification.model.enums.NotificationStatus;
import com.lms.notification.repository.NotificationRepository;
import com.lms.notification.service.impl.NotificationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository repository;

    @InjectMocks
    private NotificationServiceImpl service;

    @Test
    void shouldSaveAndReturnNotification() {

        NotificationEvent event = new NotificationEvent();
        event.setReferenceId("APP1001");
        event.setRecipient("user@test.com");
        event.setMessage("Loan Approved");

        Notification saved = Notification.builder()
                .id("1")
                .status(NotificationStatus.SENT)
                .build();

        when(repository.save(any(Notification.class))).thenReturn(saved);

        var response = service.send(event);

        assertNotNull(response);
        verify(repository).save(any(Notification.class));
    }
}
