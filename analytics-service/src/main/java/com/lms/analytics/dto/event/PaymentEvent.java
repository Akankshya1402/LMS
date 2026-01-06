package com.lms.analytics.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEvent {

    private String loanId;
    private BigDecimal amount;
    private boolean emiPayment; // true = EMI, false = disbursement
}
