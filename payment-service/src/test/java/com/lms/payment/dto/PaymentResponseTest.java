package com.lms.payment.dto;

import com.lms.payment.model.enums.PaymentStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PaymentResponseTest {

    @Test
    void shouldBuildPaymentResponse() {

        PaymentResponse response = PaymentResponse.builder()
                .paymentId("PAY1")
                .loanId("LN1")
                .emiNumber(1)
                .amount(BigDecimal.TEN)
                .status(PaymentStatus.SUCCESS)
                .createdAt(LocalDateTime.now())
                .build();

        assertEquals("PAY1", response.getPaymentId());
        assertEquals(PaymentStatus.SUCCESS, response.getStatus());
    }
}
