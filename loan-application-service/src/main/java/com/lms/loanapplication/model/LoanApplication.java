package com.lms.loanapplication.model;

import com.lms.loanapplication.model.enums.ApplicationStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "loan_applications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplication {

    @Id
    private String applicationId;

    private String customerId;

    private String loanType;           // PERSONAL, HOME, VEHICLE, EDUCATION
    private BigDecimal loanAmount;
    private Integer tenureMonths;
    private BigDecimal monthlyIncome;

    private ApplicationStatus status;

    private String remarks;

    private LocalDateTime createdAt;
    private LocalDateTime reviewedAt;
    private String reviewedBy;
}
