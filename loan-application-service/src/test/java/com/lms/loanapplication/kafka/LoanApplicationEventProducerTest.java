package com.lms.loanapplication.kafka;

import com.lms.loanapplication.model.LoanApplication;
import com.lms.loanapplication.model.enums.ApplicationStatus;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanApplicationEventProducerTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private LoanApplicationEventProducer producer;

    @Test
    void shouldPublishApplicationCreatedEvent() {

        LoanApplication application = LoanApplication.builder()
                .applicationId("APP1")
                .customerId("CUST1")
                .loanType("PERSONAL")
                .loanAmount(BigDecimal.valueOf(100000))
                .tenureMonths(12)
                .status(ApplicationStatus.APPLIED)
                .build();

        producer.publishApplicationCreated(application);

        verify(kafkaTemplate)
                .send(eq(KafkaTopics.LOAN_APP_CREATED), any());
    }

    @Test
    void shouldPublishApplicationApprovedEvent() {

        LoanApplication application = LoanApplication.builder()
                .applicationId("APP2")
                .status(ApplicationStatus.APPROVED)
                .build();

        producer.publishApplicationApproved(application);

        verify(kafkaTemplate)
                .send(eq(KafkaTopics.LOAN_APP_APPROVED), any());
    }

    @Test
    void shouldPublishApplicationRejectedEvent() {

        LoanApplication application = LoanApplication.builder()
                .applicationId("APP3")
                .status(ApplicationStatus.REJECTED)
                .build();

        producer.publishApplicationRejected(application);

        verify(kafkaTemplate)
                .send(eq(KafkaTopics.LOAN_APP_REJECTED), any());
    }
}

