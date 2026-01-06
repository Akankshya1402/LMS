package com.lms.notification.consumer;

import com.lms.notification.dto.LoanApprovedEvent;
import com.lms.notification.model.Notification;
import com.lms.notification.model.enums.NotificationChannel;
import com.lms.notification.model.enums.NotificationStatus;
import com.lms.notification.repository.NotificationRepository;
import com.lms.notification.service.EmailSenderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LoanApprovedConsumerTest {

    @Mock
    private EmailSenderService emailSenderService;

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private LoanApprovedConsumer consumer;

    @Test
    void shouldSendEmailAndSaveNotification() {

        LoanApprovedEvent event = new LoanApprovedEvent();
        event.setApplicationId("APP123");
        event.setCustomerEmail("user@mail.com");
        event.setLoanAmount(150000.0);

        consumer.handleLoanApproved(event);

        verify(emailSenderService).sendEmail(
                anyString(),
                anyString(),
                anyString()
        );

        ArgumentCaptor<Notification> captor =
                ArgumentCaptor.forClass(Notification.class);

        verify(notificationRepository).save(captor.capture());

        Notification saved = captor.getValue();

        assertEquals("APP123", saved.getReferenceId());
        assertEquals("user@mail.com", saved.getRecipient());
        assertEquals(NotificationChannel.EMAIL, saved.getChannel());
        assertEquals(NotificationStatus.SENT, saved.getStatus());
    }

}
