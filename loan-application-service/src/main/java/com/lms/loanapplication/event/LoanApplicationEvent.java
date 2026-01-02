package com.lms.loanapplication.event;

import com.lms.loanapplication.model.enums.ApplicationStatus;
import com.lms.loanapplication.model.enums.LoanType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class LoanApplicationEvent {

    private String applicationId;
    private String customerId;

    // ✅ MUST BE ENUM
    private LoanType loanType;

    private BigDecimal loanAmount;
    private Integer tenureMonths;

    private ApplicationStatus status;
    private LocalDateTime eventTime;
}
