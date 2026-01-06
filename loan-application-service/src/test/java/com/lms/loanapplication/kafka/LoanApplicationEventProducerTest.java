package com.lms.loanapplication.kafka;

import com.lms.loanapplication.model.LoanApplication;
import com.lms.loanapplication.model.enums.*;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LoanApplicationEventProducerTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private LoanApplicationEventProducer producer;

    @Test
    void shouldPublishLoanApplicationSubmittedEvent() {

        LoanApplication application = LoanApplication.builder()
                .applicationId("APP1")
                .customerId("C1")
                .loanType(LoanType.PERSONAL)
                .loanAmount(BigDecimal.valueOf(100000))
                .tenureMonths(12)
                .status(ApplicationStatus.SUBMITTED)
                .build();

        producer.publishSubmitted(application);

        verify(kafkaTemplate)
                .send(eq(KafkaTopics.LOAN_APPLICATION_SUBMITTED), any());
    }
}
