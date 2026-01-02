package com.lms.loanprocessing.consumer;

import com.lms.loanprocessing.event.LoanApprovedEvent;
import com.lms.loanprocessing.service.LoanProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoanApprovedEventConsumer {

    private final LoanProcessingService service;

    @KafkaListener(
            topics = "loan-application-approved",
            groupId = "loan-processing-group",
            containerFactory = "loanApprovedKafkaListenerFactory"
    )
    public void consume(LoanApprovedEvent event) {
        log.info("✅ Loan approved event received: {}", event);
        service.processApprovedApplication(event);
    }
}




