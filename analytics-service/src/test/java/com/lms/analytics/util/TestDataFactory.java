package com.lms.analytics.util;

import com.lms.analytics.model.CustomerAnalytics;
import com.lms.analytics.model.LoanAnalytics;
import com.lms.analytics.model.PaymentAnalytics;

import java.math.BigDecimal;

public final class TestDataFactory {

    private TestDataFactory() {}

    public static LoanAnalytics loanAnalytics() {
        return LoanAnalytics.builder()
                .loanType("PERSONAL")
                .totalApplications(10)
                .approvedCount(5)
                .pendingCount(3)
                .rejectedCount(2)
                .totalDisbursedAmount(BigDecimal.valueOf(500000))
                .build();
    }

    public static CustomerAnalytics customerAnalytics() {
        return CustomerAnalytics.builder()
                .activeCustomers(25)
                .build();
    }

    public static PaymentAnalytics paymentAnalytics() {
        return PaymentAnalytics.builder()
                .totalCollected(BigDecimal.valueOf(120000))
                .build();
    }
}
