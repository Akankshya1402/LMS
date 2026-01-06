package com.lms.analytics.consumer;

import com.lms.analytics.dto.event.PaymentEvent;
import com.lms.analytics.service.AnalyticsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PaymentAnalyticsConsumerTest {

    @Mock
    private AnalyticsService analyticsService;

    @InjectMocks
    private PaymentAnalyticsConsumer consumer;

    @Test
    void shouldConsumePaymentEvent() {

        PaymentEvent event = PaymentEvent.builder()
                .loanId("L1")
                .amount(BigDecimal.valueOf(2000))
                .emiPayment(true)
                .build();

        consumer.consumePaymentEvent(event);

        verify(analyticsService)
                .processPaymentEvent(event);
    }
}
