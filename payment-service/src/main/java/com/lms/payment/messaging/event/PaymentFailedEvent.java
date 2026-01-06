package com.lms.payment.messaging.event;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PaymentFailedEvent {

    private String customerId;
    private String paymentId;
    private String loanId;
    private Integer emiNumber;
    private BigDecimal amount;
    private String reason;
}

