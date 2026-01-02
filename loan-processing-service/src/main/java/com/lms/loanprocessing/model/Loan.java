package com.lms.loanprocessing.model;

import com.lms.loanprocessing.model.enums.LoanStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "loans")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Loan {

    @Id
    private String loanId;

    private String applicationId;
    private String customerId;

    private BigDecimal principal;
    private BigDecimal interestRate;
    private Integer tenureMonths;

    private BigDecimal emiAmount;
    private BigDecimal outstandingAmount;

    private LoanStatus status;

    private LocalDateTime disbursedAt;
    private LocalDateTime closedAt;
}
