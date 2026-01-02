package com.lms.loanprocessing.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanApprovedEvent {

    private String applicationId;
    private String customerId;
    private String customerEmail;
    private BigDecimal approvedAmount;
    private Integer tenureMonths;
    private Double interestRate;
}

