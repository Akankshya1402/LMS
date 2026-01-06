package com.lms.loanapplication.dto;

import com.lms.loanapplication.model.enums.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class LoanApplicationResponse {

    private String applicationId;
    private String customerId;
    private LoanType loanType;
    private BigDecimal loanAmount;
    private Integer tenureMonths;
    private ApplicationStatus status;
    private LocalDateTime appliedAt;
}
