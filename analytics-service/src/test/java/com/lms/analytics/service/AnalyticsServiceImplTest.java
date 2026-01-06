package com.lms.analytics.service;

import com.lms.analytics.dto.response.DashboardResponse;
import com.lms.analytics.model.CustomerAnalytics;
import com.lms.analytics.model.LoanAnalytics;
import com.lms.analytics.model.PaymentAnalytics;
import com.lms.analytics.repository.CustomerAnalyticsRepository;
import com.lms.analytics.repository.LoanAnalyticsRepository;
import com.lms.analytics.repository.PaymentAnalyticsRepository;
import com.lms.analytics.service.impl.AnalyticsServiceImpl;
import com.lms.analytics.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceImplTest {

    @Mock
    private LoanAnalyticsRepository loanRepo;

    @Mock
    private CustomerAnalyticsRepository customerRepo;

    @Mock
    private PaymentAnalyticsRepository paymentRepo;

    @InjectMocks
    private AnalyticsServiceImpl service;

    @Test
    void shouldReturnDashboardDataSuccessfully() {

        LoanAnalytics loanAnalytics = TestDataFactory.loanAnalytics();
        CustomerAnalytics customerAnalytics = TestDataFactory.customerAnalytics();
        PaymentAnalytics paymentAnalytics = TestDataFactory.paymentAnalytics();

        when(loanRepo.findAll()).thenReturn(List.of(loanAnalytics));
        when(customerRepo.findById("GLOBAL"))
                .thenReturn(Optional.of(customerAnalytics));
        when(paymentRepo.findById("GLOBAL"))
                .thenReturn(Optional.of(paymentAnalytics));

        DashboardResponse response = service.getDashboard();

        assertEquals(10, response.getTotalLoans());
        assertEquals(5, response.getApprovedLoans());
        assertEquals(3, response.getPendingLoans());
        assertEquals(2, response.getRejectedLoans());
        assertEquals(BigDecimal.valueOf(500000), response.getTotalDisbursedAmount());
        assertEquals(25, response.getActiveCustomers());
        assertEquals(BigDecimal.valueOf(120000), response.getTotalEmiCollected());
    }

    @Test
    void shouldHandleEmptyRepositoriesGracefully() {

        when(loanRepo.findAll()).thenReturn(List.of());
        when(customerRepo.findById("GLOBAL")).thenReturn(Optional.empty());
        when(paymentRepo.findById("GLOBAL")).thenReturn(Optional.empty());

        DashboardResponse response = service.getDashboard();

        assertEquals(0, response.getTotalLoans());
        assertEquals(0, response.getApprovedLoans());
        assertEquals(BigDecimal.ZERO, response.getTotalDisbursedAmount());
        assertEquals(0, response.getActiveCustomers());
        assertEquals(BigDecimal.ZERO, response.getTotalEmiCollected());
    }
}
