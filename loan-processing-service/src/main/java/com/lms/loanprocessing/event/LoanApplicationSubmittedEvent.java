package com.lms.loanprocessing.event;

import com.lms.loanprocessing.model.enums.LoanType;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanApplicationSubmittedEvent {

    private String applicationId;
    private String customerId;
    private LoanType loanType;
    private BigDecimal requestedAmount;
    private Integer tenureMonths;
}
