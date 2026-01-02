package com.lms.loanapplication.service;

import com.lms.loanapplication.client.CustomerClient;
import com.lms.loanapplication.dto.LoanApplicationRequest;
import com.lms.loanapplication.dto.LoanApplicationResponse;
import com.lms.loanapplication.kafka.LoanApplicationEventProducer;
import com.lms.loanapplication.model.LoanApplication;
import com.lms.loanapplication.model.enums.ApplicationStatus;
import com.lms.loanapplication.model.enums.LoanType;
import com.lms.loanapplication.repository.LoanApplicationRepository;
import com.lms.loanapplication.service.impl.LoanApplicationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanApplicationServiceTest {

    @Mock
    private LoanApplicationRepository repository;

    @Mock
    private LoanApplicationEventProducer eventProducer;

    @Mock
    private CustomerClient customerClient;

    @InjectMocks
    private LoanApplicationServiceImpl service;

    // =========================
    // APPLY
    // =========================
    @Test
    void shouldApplyForLoanAndPublishEvent() {

        LoanApplicationRequest request = new LoanApplicationRequest();
        request.setLoanType(LoanType.PERSONAL);
        request.setLoanAmount(BigDecimal.valueOf(200000));
        request.setTenureMonths(24);
        request.setMonthlyIncome(BigDecimal.valueOf(50000));

        LoanApplication saved = LoanApplication.builder()
                .applicationId("APP1")
                .customerId("CUST1")
                .loanType(LoanType.PERSONAL)
                .loanAmount(request.getLoanAmount())
                .tenureMonths(24)
                .monthlyIncome(request.getMonthlyIncome())
                .status(ApplicationStatus.APPLIED)
                .createdAt(LocalDateTime.now())
                .build();

        doNothing().when(customerClient).validateCustomerForLoan("CUST1");
        when(repository.existsByCustomerIdAndLoanTypeAndStatus(
                any(), any(), any())).thenReturn(false);
        when(repository.save(any())).thenReturn(saved);

        LoanApplicationResponse response =
                service.apply("CUST1", request);

        assertEquals("APP1", response.getApplicationId());
        assertEquals(ApplicationStatus.APPLIED, response.getStatus());

        verify(eventProducer).publishApplicationCreated(any());
        verify(repository).save(any());
    }

    // =========================
    // GET MY APPLICATIONS
    // =========================
    @Test
    void shouldReturnCustomerApplications() {

        when(repository.findByCustomerId("C1"))
                .thenReturn(List.of(
                        LoanApplication.builder()
                                .applicationId("APP1")
                                .status(ApplicationStatus.APPLIED)
                                .build()
                ));

        List<LoanApplicationResponse> responses =
                service.getMyApplications("C1");

        assertEquals(1, responses.size());
        verify(repository).findByCustomerId("C1");
    }

    // =========================
    // REVIEW APPROVE
    // =========================
    @Test
    void shouldApproveApplication() {

        LoanApplication app = LoanApplication.builder()
                .applicationId("APP1")
                .customerId("C1")
                .loanAmount(BigDecimal.valueOf(120000))
                .tenureMonths(12)
                .status(ApplicationStatus.APPLIED)
                .build();

        when(repository.findById("APP1")).thenReturn(Optional.of(app));
        when(customerClient.getCreditScore("C1")).thenReturn(700);
        when(repository.save(any())).thenReturn(app);

        LoanApplicationResponse response =
                service.review("APP1", true, "OK", "OFFICER");

        assertEquals(ApplicationStatus.APPROVED, response.getStatus());
        verify(eventProducer).publishApplicationApproved(any());
    }

    // =========================
    // REVIEW REJECT
    // =========================
    @Test
    void shouldRejectApplication() {

        LoanApplication app = LoanApplication.builder()
                .applicationId("APP1")
                .status(ApplicationStatus.APPLIED)
                .build();

        when(repository.findById("APP1")).thenReturn(Optional.of(app));
        when(repository.save(any())).thenReturn(app);

        LoanApplicationResponse response =
                service.review("APP1", false, "Rejected", "OFFICER");

        assertEquals(ApplicationStatus.REJECTED, response.getStatus());
        verify(eventProducer).publishApplicationRejected(any());
    }
}

