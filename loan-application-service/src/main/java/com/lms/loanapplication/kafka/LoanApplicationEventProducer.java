package com.lms.loanapplication.kafka;

import com.lms.loanapplication.event.LoanApplicationSubmittedEvent;
import com.lms.loanapplication.model.LoanApplication;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class LoanApplicationEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishSubmitted(LoanApplication app) {

        LoanApplicationSubmittedEvent event =
                LoanApplicationSubmittedEvent.builder()
                        .applicationId(app.getApplicationId())
                        .customerId(app.getCustomerId())
                        .loanType(app.getLoanType())
                        .requestedAmount(app.getLoanAmount())
                        .tenureMonths(app.getTenureMonths())
                        .status(app.getStatus())
                        .eventTime(LocalDateTime.now())
                        .build();

        kafkaTemplate.send(
                KafkaTopics.LOAN_APPLICATION_SUBMITTED,
                event
        );
    }
}

