package com.lms.analytics.consumer;

import com.lms.analytics.dto.event.LoanEvent;
import com.lms.analytics.service.AnalyticsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LoanAnalyticsConsumerTest {

    @Mock
    private AnalyticsService analyticsService;

    @InjectMocks
    private LoanAnalyticsConsumer consumer;

    @Test
    void shouldConsumeLoanEvent() {

        LoanEvent event = LoanEvent.builder()
                .loanId("L1")
                .status("APPROVED")
                .amount(BigDecimal.valueOf(500000))
                .build();

        consumer.consumeLoanEvent(event);

        verify(analyticsService)
                .processLoanEvent(event);
    }
}

