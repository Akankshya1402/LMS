package com.lms.payment.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentExceptionTest {

    @Test
    void paymentAlreadyProcessedExceptionMessage() {
        Exception ex = new PaymentAlreadyProcessedException("EMI already paid");
        assertEquals("EMI already paid", ex.getMessage());
    }

    @Test
    void paymentFailedExceptionMessage() {
        Exception ex = new PaymentFailedException("Payment failed");
        assertEquals("Payment failed", ex.getMessage());
    }

    @Test
    void invalidEmiExceptionMessage() {
        Exception ex = new InvalidEmiException("Invalid EMI");
        assertEquals("Invalid EMI", ex.getMessage());
    }
  

}
