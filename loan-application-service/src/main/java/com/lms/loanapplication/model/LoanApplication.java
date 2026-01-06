package com.lms.loanapplication.model;

import com.lms.loanapplication.model.enums.*;
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
    private LoanType loanType;
    private BigDecimal loanAmount;
    private Integer tenureMonths;
    private ApplicationStatus status;
    private LocalDateTime createdAt;
}
