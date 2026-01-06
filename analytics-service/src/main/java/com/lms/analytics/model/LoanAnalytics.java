package com.lms.analytics.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "loan_analytics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanAnalytics {

    @Id
    private String loanType;

    private long totalApplications;
    private long approvedCount;
    private long pendingCount;
    private long rejectedCount;

    private BigDecimal totalDisbursedAmount;
}
