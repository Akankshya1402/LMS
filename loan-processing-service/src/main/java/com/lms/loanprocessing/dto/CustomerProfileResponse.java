package com.lms.loanprocessing.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CustomerProfileResponse {

    private Integer creditScore;
    private BigDecimal monthlyIncome;
    private BigDecimal existingEmiLiability;
    private String email;
}
