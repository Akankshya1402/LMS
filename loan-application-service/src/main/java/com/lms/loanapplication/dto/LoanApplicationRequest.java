package com.lms.loanapplication.dto;

import com.lms.loanapplication.model.enums.LoanType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LoanApplicationRequest {

    // =========================
    // LOAN TYPE (ENUM → DROPDOWN)
    // =========================
    @NotNull(message = "Loan type is required")
    private LoanType loanType;

    // =========================
    // LOAN AMOUNT
    // =========================
    @NotNull(message = "Loan amount is required")
    @DecimalMin(value = "10000", message = "Minimum loan amount is 10,000")
    @DecimalMax(value = "1000000", message = "Maximum loan amount is 10,00,000")
    private BigDecimal loanAmount;

    // =========================
    // TENURE (NUMERIC)
    // =========================
    @NotNull(message = "Tenure is required")
    @Min(value = 12, message = "Minimum tenure is 12 months")
    @Max(value = 36, message = "Maximum tenure is 36 months")
    private Integer tenureMonths;

    // =========================
    // MONTHLY INCOME
    // =========================
    @NotNull(message = "Monthly income is required")
    @DecimalMin(
        value = "0.0",
        inclusive = false,
        message = "Monthly income must be greater than zero"
    )
    private BigDecimal monthlyIncome;
}


