package com.lms.payment.messaging.event;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PaymentSuccessEvent {

    private String paymentId;
    private String customerId;
    private String loanId;
    private Integer emiNumber;
    private BigDecimal amount;
}
