package com.lms.notification.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailSenderServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailSenderService service;

    @Test
    void shouldSendEmailUsingMailSender() {

        service.sendEmail(
                "user@mail.com",
                "Loan Approved",
                "Congratulations!"
        );

        ArgumentCaptor<SimpleMailMessage> captor =
                ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(mailSender).send(captor.capture());

        SimpleMailMessage message = captor.getValue();

        assertEquals("user@mail.com", message.getTo()[0]);
        assertEquals("Loan Approved", message.getSubject());
        assertEquals("Congratulations!", message.getText());
    }
}
