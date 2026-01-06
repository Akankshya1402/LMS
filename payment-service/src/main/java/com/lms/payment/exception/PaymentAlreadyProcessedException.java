package com.lms.payment.exception;

public class PaymentAlreadyProcessedException extends RuntimeException {

    public PaymentAlreadyProcessedException(String message) {
        super(message);
    }
}
