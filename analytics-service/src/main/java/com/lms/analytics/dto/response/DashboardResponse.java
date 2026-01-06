package com.lms.analytics.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    private long totalLoans;
    private long approvedLoans;
    private long pendingLoans;
    private long rejectedLoans;

    private BigDecimal totalDisbursedAmount;

    private long activeCustomers;

    private BigDecimal totalEmiCollected;
}
