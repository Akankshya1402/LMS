package com.lms.loanapplication.event;

import com.lms.loanapplication.model.enums.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplicationSubmittedEvent {

    private String applicationId;
    private String customerId;
    private LoanType loanType;
    private BigDecimal requestedAmount;
    private Integer tenureMonths;
    private ApplicationStatus status;
    private LocalDateTime eventTime;
}
