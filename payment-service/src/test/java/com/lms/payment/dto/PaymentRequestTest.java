package com.lms.payment.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PaymentRequestTest {

    @Test
    void shouldSetAndGetFields() {
        PaymentRequest request = new PaymentRequest();

        request.setLoanId("LN1");
        request.setCustomerId("CUST1");
        request.setEmiNumber(1);
        request.setAmount(BigDecimal.valueOf(5000));

        assertEquals("LN1", request.getLoanId());
        assertEquals("CUST1", request.getCustomerId());
        assertEquals(1, request.getEmiNumber());
        assertEquals(BigDecimal.valueOf(5000), request.getAmount());
    }
}
