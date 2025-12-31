package com.lms.loanapplication.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LoanApplicationRequest {

    @NotBlank(message = "Loan type is required")
    private String loanType;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal loanAmount;

    @NotNull
    @Min(6)
    @Max(360)
    private Integer tenureMonths;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal monthlyIncome;
}


