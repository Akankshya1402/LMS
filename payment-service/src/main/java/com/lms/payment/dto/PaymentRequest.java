package com.lms.payment.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {

    @NotBlank
    private String loanId;

    @NotBlank
    private String customerId;

    @NotNull
    @Min(1)
    private Integer emiNumber;

    @NotNull
    @Min(1)
    private BigDecimal amount;
}
