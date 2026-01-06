package com.lms.analytics.consumer;

import com.lms.analytics.dto.event.LoanEvent;
import com.lms.analytics.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoanAnalyticsConsumer {

    private final AnalyticsService analyticsService;

    @KafkaListener(
            topics = "loan-events",
            groupId = "analytics-service"
    )
    public void consumeLoanEvent(LoanEvent event) {

        log.info("Loan event received: {}", event);

        analyticsService.processLoanEvent(event);
    }
}
