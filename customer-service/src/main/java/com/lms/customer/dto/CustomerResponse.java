package com.lms.customer.dto;

import com.lms.customer.model.enums.AccountStatus;
import com.lms.customer.model.enums.KycStatus;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {

    private String customerId;
    private String fullName;
    private String email;
    private String mobile;

    private BigDecimal monthlyIncome;
    private Integer creditScore;

    private AccountStatus accountStatus;
    private KycStatus kycStatus;
}
