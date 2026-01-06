package com.lms.loanapplication.dto;

import com.lms.loanapplication.model.enums.LoanType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LoanApplicationRequest {

    @NotNull
    private LoanType loanType;

    @NotNull
    @DecimalMin("10000")
    @DecimalMax("1000000")
    private BigDecimal loanAmount;

    @NotNull
    @Min(12) @Max(36)
    private Integer tenureMonths;
}

