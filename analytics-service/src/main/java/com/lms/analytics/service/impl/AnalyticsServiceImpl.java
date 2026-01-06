package com.lms.analytics.service.impl;

import com.lms.analytics.dto.event.CustomerEvent;
import com.lms.analytics.dto.event.LoanEvent;
import com.lms.analytics.dto.event.PaymentEvent;
import com.lms.analytics.dto.response.DashboardResponse;
import com.lms.analytics.model.CustomerAnalytics;
import com.lms.analytics.model.LoanAnalytics;
import com.lms.analytics.model.PaymentAnalytics;
import com.lms.analytics.repository.CustomerAnalyticsRepository;
import com.lms.analytics.repository.LoanAnalyticsRepository;
import com.lms.analytics.repository.PaymentAnalyticsRepository;
import com.lms.analytics.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final LoanAnalyticsRepository loanAnalyticsRepository;
    private final CustomerAnalyticsRepository customerAnalyticsRepository;
    private final PaymentAnalyticsRepository paymentAnalyticsRepository;

    // =========================
    // LOAN EVENTS
    // =========================
    @Override
    public void processLoanEvent(LoanEvent event) {

        LoanAnalytics analytics = loanAnalyticsRepository
                .findById(event.getLoanType())
                .orElse(LoanAnalytics.builder()
                        .loanType(event.getLoanType())
                        .totalApplications(0)
                        .approvedCount(0)
                        .pendingCount(0)
                        .rejectedCount(0)
                        .totalDisbursedAmount(BigDecimal.ZERO)
                        .build());

        analytics.setTotalApplications(analytics.getTotalApplications() + 1);

        switch (event.getStatus()) {
            case "APPROVED" -> {
                analytics.setApprovedCount(analytics.getApprovedCount() + 1);
                analytics.setTotalDisbursedAmount(
                        analytics.getTotalDisbursedAmount().add(event.getAmount())
                );
            }
            case "PENDING" ->
                    analytics.setPendingCount(analytics.getPendingCount() + 1);
            case "REJECTED" ->
                    analytics.setRejectedCount(analytics.getRejectedCount() + 1);
        }

        loanAnalyticsRepository.save(analytics);
    }

    // =========================
    // CUSTOMER EVENTS
    // =========================
    @Override
    public void processCustomerEvent(CustomerEvent event) {

        CustomerAnalytics analytics = customerAnalyticsRepository
                .findById("GLOBAL")
                .orElse(CustomerAnalytics.builder()
                        .id("GLOBAL")
                        .activeCustomers(0)
                        .build());

        if (event.isActive()) {
            analytics.setActiveCustomers(analytics.getActiveCustomers() + 1);
        }

        customerAnalyticsRepository.save(analytics);
    }

    // =========================
    // PAYMENT EVENTS
    // =========================
    @Override
    public void processPaymentEvent(PaymentEvent event) {

        PaymentAnalytics analytics = paymentAnalyticsRepository
                .findById("GLOBAL")
                .orElse(PaymentAnalytics.builder()
                        .id("GLOBAL")
                        .totalCollected(BigDecimal.ZERO)
                        .build());

        analytics.setTotalCollected(
                analytics.getTotalCollected().add(event.getAmount())
        );

        paymentAnalyticsRepository.save(analytics);
    }

    // =========================
    // DASHBOARD READ
    // =========================
    @Override
    public DashboardResponse getDashboard() {

        List<LoanAnalytics> loanStats = loanAnalyticsRepository.findAll();

        long totalLoans = loanStats.stream()
                .mapToLong(LoanAnalytics::getTotalApplications)
                .sum();

        long approved = loanStats.stream()
                .mapToLong(LoanAnalytics::getApprovedCount)
                .sum();

        long pending = loanStats.stream()
                .mapToLong(LoanAnalytics::getPendingCount)
                .sum();

        long rejected = loanStats.stream()
                .mapToLong(LoanAnalytics::getRejectedCount)
                .sum();

        BigDecimal totalDisbursed = loanStats.stream()
                .map(LoanAnalytics::getTotalDisbursedAmount)
                .filter(v -> v != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        CustomerAnalytics customerAnalytics =
                customerAnalyticsRepository
                        .findById("GLOBAL")
                        .orElse(CustomerAnalytics.builder()
                                .activeCustomers(0)
                                .build());

        PaymentAnalytics paymentAnalytics =
                paymentAnalyticsRepository
                        .findById("GLOBAL")
                        .orElse(PaymentAnalytics.builder()
                                .totalCollected(BigDecimal.ZERO)
                                .build());

        return DashboardResponse.builder()
                .totalLoans(totalLoans)
                .approvedLoans(approved)
                .pendingLoans(pending)
                .rejectedLoans(rejected)
                .totalDisbursedAmount(totalDisbursed)
                .activeCustomers(customerAnalytics.getActiveCustomers())
                .totalEmiCollected(paymentAnalytics.getTotalCollected())
                .build();
    }
}
