package com.lms.loanapplication.event;

import com.lms.loanapplication.model.enums.ApplicationStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplicationEvent {

    private String applicationId;
    private String customerId;
    private String loanType;
    private BigDecimal loanAmount;
    private Integer tenureMonths;
    private ApplicationStatus status;
    private LocalDateTime eventTime;
}
