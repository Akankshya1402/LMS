package com.lms.loanprocessing.consumer;

import com.lms.loanprocessing.event.EmiPaidEvent;
import com.lms.loanprocessing.service.LoanServicingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmiPaidEventConsumer {

    private final LoanServicingService servicingService;

    @KafkaListener(
            topics = "emi-paid",
            groupId = "loan-processing-group"
    )
    public void consume(EmiPaidEvent event) {
        log.info("💰 EMI paid for loan {}, EMI #{}",
                event.getLoanId(),
                event.getEmiNumber());

        servicingService.markEmiPaid(
                event.getLoanId(),
                event.getEmiNumber());
    }
}

