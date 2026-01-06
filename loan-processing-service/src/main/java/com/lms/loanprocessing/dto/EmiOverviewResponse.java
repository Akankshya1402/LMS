package com.lms.loanprocessing.dto;

import com.lms.loanprocessing.model.enums.LoanStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class EmiOverviewResponse {

    private String loanId;

    private int totalEmis;
    private int paidEmis;
    private int pendingEmis;
    private int overdueEmis;

    private BigDecimal emiAmount;
    private BigDecimal outstandingAmount;

    private LoanStatus loanStatus;
}
