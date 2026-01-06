package com.lms.payment.messaging;

import com.lms.payment.messaging.event.PaymentFailedEvent;
import com.lms.payment.messaging.event.PaymentSuccessEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PaymentEventProducerTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private PaymentEventProducer producer;

    @Test
    void shouldPublishPaymentSuccessEvent() {

        PaymentSuccessEvent event = PaymentSuccessEvent.builder()
                .paymentId("P1")
                .loanId("L1")
                .emiNumber(1)
                .build();

        producer.publishPaymentSuccess(event);

        verify(kafkaTemplate).send(
                eq("payment.success"),
                eq("P1"),
                eq(event)
        );
    }

    @Test
    void shouldPublishPaymentFailureEvent() {

        PaymentFailedEvent event = PaymentFailedEvent.builder()
                .paymentId("P1")
                .loanId("L1")
                .reason("FAILED")
                .build();

        producer.publishPaymentFailure(event);

        verify(kafkaTemplate).send(
                eq("payment.failed"),
                eq("P1"),
                eq(event)
        );
    }
}
