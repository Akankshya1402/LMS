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
public class LoanEvent {

    private String loanId;
    private String loanType;     // PERSONAL, HOME, CAR
    private String status;       // APPLIED, APPROVED, REJECTED
    private BigDecimal amount;
}
