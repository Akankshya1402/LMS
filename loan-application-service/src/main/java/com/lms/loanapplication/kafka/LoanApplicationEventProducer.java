package com.lms.loanapplication.kafka;

import com.lms.loanapplication.event.LoanApplicationEvent;
import com.lms.loanapplication.model.LoanApplication;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class LoanApplicationEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishApplicationCreated(LoanApplication application) {
        kafkaTemplate.send(
                KafkaTopics.LOAN_APP_CREATED,
                buildEvent(application)
        );
    }

    public void publishApplicationApproved(LoanApplication application) {
        kafkaTemplate.send(
                KafkaTopics.LOAN_APP_APPROVED,
                buildEvent(application)
        );
    }

    public void publishApplicationRejected(LoanApplication application) {
        kafkaTemplate.send(
                KafkaTopics.LOAN_APP_REJECTED,
                buildEvent(application)
        );
    }

    private LoanApplicationEvent buildEvent(LoanApplication app) {
        return LoanApplicationEvent.builder()
                .applicationId(app.getApplicationId())
                .customerId(app.getCustomerId())
                // ✅ ENUM SAFE
                .loanType(app.getLoanType())
                .loanAmount(app.getLoanAmount())
                .tenureMonths(app.getTenureMonths())
                .status(app.getStatus())
                .eventTime(LocalDateTime.now())
                .build();
    }
}

