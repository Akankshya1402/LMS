package com.lms.analytics.consumer;

import com.lms.analytics.dto.event.CustomerEvent;
import com.lms.analytics.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerAnalyticsConsumer {

    private final AnalyticsService analyticsService;

    @KafkaListener(
            topics = "customer-events",
            groupId = "analytics-service"
    )
    public void consumeCustomerEvent(CustomerEvent event) {

        log.info("Customer event received: {}", event);
        analyticsService.processCustomerEvent(event);
    }
}


