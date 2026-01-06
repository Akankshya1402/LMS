package com.lms.analytics.consumer;

import com.lms.analytics.dto.event.PaymentEvent;
import com.lms.analytics.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentAnalyticsConsumer {

    private final AnalyticsService analyticsService;

    @KafkaListener(
            topics = "payment-events",
            groupId = "analytics-service"
    )
    public void consumePaymentEvent(PaymentEvent event) {

        log.info("Payment event received: {}", event);

        analyticsService.processPaymentEvent(event);
    }
}
