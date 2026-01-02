package com.lms.notification.consumer;

import com.lms.notification.dto.LoanApprovedEvent;
import com.lms.notification.model.Notification;
import com.lms.notification.model.enums.NotificationChannel;
import com.lms.notification.model.enums.NotificationStatus;
import com.lms.notification.repository.NotificationRepository;
import com.lms.notification.service.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class LoanApprovedConsumer {

    private final EmailSenderService emailSenderService;
    private final NotificationRepository repository;

    @KafkaListener(
            topics = "loan-approved",
            groupId = "notification-group"
    )
    public void handleLoanApproved(LoanApprovedEvent event) {

        String message = """
                Dear Customer,

                🎉 Your loan has been APPROVED!

                Application ID: %s
                Loan Amount: ₹%.2f

                Thank you for choosing our Loan Management System.
                """
                .formatted(event.getApplicationId(), event.getLoanAmount());

        // ✅ Send email
        emailSenderService.sendEmail(
                event.getCustomerEmail(),
                "Loan Approved Successfully",
                message
        );

        // ✅ Save notification
        Notification notification = Notification.builder()
                .referenceId(event.getApplicationId())
                .recipient(event.getCustomerEmail())
                .message(message)
                .channel(NotificationChannel.EMAIL)
                .status(NotificationStatus.SENT)
                .createdAt(LocalDateTime.now())
                .build();

        repository.save(notification);
    }
}

