package com.lms.loanapplication.dto;

import com.lms.loanapplication.model.enums.ApplicationStatus;
import com.lms.loanapplication.model.enums.LoanType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class LoanApplicationResponse {

    private String applicationId;
    private String customerId;

    private LoanType loanType;   // <-- MUST BE ENUM

    private BigDecimal loanAmount;
    private Integer tenureMonths;
    private BigDecimal monthlyIncome;

    private ApplicationStatus status;
    private String remarks;

    private LocalDateTime appliedAt;
}
