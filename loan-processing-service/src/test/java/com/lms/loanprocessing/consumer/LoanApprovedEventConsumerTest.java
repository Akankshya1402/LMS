package com.lms.loanprocessing.consumer;

import com.lms.loanprocessing.event.LoanApprovedEvent;
import com.lms.loanprocessing.service.LoanProcessingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LoanApprovedEventConsumerTest {

    @Mock
    private LoanProcessingService service;

    @InjectMocks
    private LoanApprovedEventConsumer consumer;

    @Test
    void shouldDelegateEventToService() {

        LoanApprovedEvent event = LoanApprovedEvent.builder()
                .applicationId("APP1")
                .customerId("C1")
                .customerEmail("test@gmail.com")
                .approvedAmount(BigDecimal.valueOf(100000))
                .tenureMonths(12)
                .interestRate(10.5)
                .build();

        consumer.consume(event);

        verify(service).processApprovedApplication(event);
    }
}
