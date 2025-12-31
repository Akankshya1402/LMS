package com.lms.customer.model;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.lms.customer.model.enums.AccountStatus;
import com.lms.customer.model.enums.KycStatus;


import java.math.BigDecimal;

@Document(collection = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    private String customerId;

    @NotBlank(message = "Full name is mandatory")
    private String fullName;

    @Email(message = "Invalid email address")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @NotBlank(message = "Mobile number is mandatory")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid mobile number")
    private String mobile;

    @NotNull(message = "Monthly income is mandatory")
    @DecimalMin(value = "0.0", inclusive = false, message = "Income must be greater than zero")
    private BigDecimal monthlyIncome;

    @Min(value = 300, message = "Invalid credit score")
    @Max(value = 900, message = "Invalid credit score")
    private Integer creditScore;

    @NotNull
    private AccountStatus accountStatus;

    private boolean emailVerified;
    private boolean mobileVerified;

    @NotNull
    private KycStatus kycStatus;
}

