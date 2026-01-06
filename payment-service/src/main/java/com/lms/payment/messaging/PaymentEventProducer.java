package com.lms.payment.messaging;

import com.lms.payment.messaging.event.PaymentFailedEvent;
import com.lms.payment.messaging.event.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishPaymentSuccess(PaymentSuccessEvent event) {
        kafkaTemplate.send(
                "payment.success",
                event.getPaymentId(),   // 🔴 THIS MUST EXIST
                event
        );
    }

    public void publishPaymentFailure(PaymentFailedEvent event) {
        kafkaTemplate.send(
                "payment.failed",
                event.getPaymentId(),
                event
        );
    }
}


