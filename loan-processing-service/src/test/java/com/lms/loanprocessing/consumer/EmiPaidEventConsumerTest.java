package com.lms.loanprocessing.consumer;

import com.lms.loanprocessing.event.EmiPaidEvent;
import com.lms.loanprocessing.service.LoanServicingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmiPaidEventConsumerTest {

    @Mock
    private LoanServicingService servicingService;

    @InjectMocks
    private EmiPaidEventConsumer consumer;

    @Test
    void shouldProcessEmiPayment() {

        EmiPaidEvent event = EmiPaidEvent.builder()
                .loanId("L1")
                .emiNumber(2)
                .build();

        consumer.consume(event);

        verify(servicingService)
               .markEmiPaid("L1", 2);
    }
}

