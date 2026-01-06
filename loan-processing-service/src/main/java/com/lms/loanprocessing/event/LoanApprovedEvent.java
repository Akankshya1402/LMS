package com.lms.loanprocessing.event;

import lombok.*;

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
