package com.lms.payment.dto;

import com.lms.payment.model.enums.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PaymentResponse {

    private String paymentId;
    private String loanId;
    private Integer emiNumber;
    private BigDecimal amount;
    private PaymentStatus status;
    private String transactionRef;
    private LocalDateTime createdAt;
}
