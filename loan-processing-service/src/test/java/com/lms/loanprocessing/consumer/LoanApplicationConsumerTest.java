package com.lms.loanprocessing.consumer;

import com.lms.loanprocessing.event.LoanApplicationSubmittedEvent;
import com.lms.loanprocessing.service.LoanApprovalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LoanApplicationConsumerTest {

    @Mock
    private LoanApprovalService approvalService;

    @InjectMocks
    private LoanApplicationConsumer consumer;

    @Test
    void shouldProcessSubmittedApplication() {

        LoanApplicationSubmittedEvent event =
                LoanApplicationSubmittedEvent.builder()
                        .applicationId("APP1")
                        .customerId("C1")
                        .requestedAmount(BigDecimal.valueOf(100000))
                        .tenureMonths(12)
                        .build();

        consumer.consume(event);

        verify(approvalService).process(event);
    }
}
