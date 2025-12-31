package com.lms.loanapplication.service;

import com.lms.loanapplication.dto.LoanApplicationRequest;
import com.lms.loanapplication.dto.LoanApplicationResponse;
import com.lms.loanapplication.kafka.LoanApplicationEventProducer;
import com.lms.loanapplication.model.LoanApplication;
import com.lms.loanapplication.model.enums.ApplicationStatus;
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

    @InjectMocks
    private LoanApplicationServiceImpl service;

    // =========================
    // APPLY FOR LOAN
    // =========================
    @Test
    void shouldApplyForLoanAndPublishEvent() {

        LoanApplicationRequest request = new LoanApplicationRequest();
        request.setLoanType("PERSONAL");
        request.setLoanAmount(BigDecimal.valueOf(200000));
        request.setTenureMonths(24);
        request.setMonthlyIncome(BigDecimal.valueOf(50000));

        LoanApplication saved = LoanApplication.builder()
                .applicationId("APP1")
                .customerId("CUST1")
                .loanType("PERSONAL")
                .loanAmount(BigDecimal.valueOf(200000))
                .tenureMonths(24)
                .monthlyIncome(BigDecimal.valueOf(50000))
                .status(ApplicationStatus.APPLIED)
                .createdAt(LocalDateTime.now())
                .build();

        when(repository.save(any(LoanApplication.class)))
                .thenReturn(saved);

        LoanApplicationResponse response =
                service.apply("CUST1", request);

        assertEquals("APP1", response.getApplicationId());
        assertEquals(ApplicationStatus.APPLIED, response.getStatus());

        verify(eventProducer)
                .publishApplicationCreated(any(LoanApplication.class));
        verify(repository).save(any(LoanApplication.class));
    }

    // =========================
    // GET CUSTOMER APPLICATIONS
    // =========================
    @Test
    void shouldReturnCustomerApplications() {

        when(repository.findByCustomerId("CUST1"))
                .thenReturn(List.of(
                        LoanApplication.builder()
                                .applicationId("APP1")
                                .status(ApplicationStatus.APPLIED)
                                .build()
                ));

        List<LoanApplicationResponse> responses =
                service.getMyApplications("CUST1");

        assertEquals(1, responses.size());
        verify(repository).findByCustomerId("CUST1");
    }

    // =========================
    // GET PENDING APPLICATIONS
    // =========================
    @Test
    void shouldReturnPendingApplications() {

        when(repository.findByStatus(ApplicationStatus.APPLIED))
                .thenReturn(List.of(
                        LoanApplication.builder()
                                .applicationId("APP1")
                                .status(ApplicationStatus.APPLIED)
                                .build()
                ));

        List<LoanApplicationResponse> responses =
                service.getPendingApplications();

        assertEquals(1, responses.size());
        verify(repository).findByStatus(ApplicationStatus.APPLIED);
    }

    // =========================
    // APPROVE APPLICATION
    // =========================
    @Test
    void shouldApproveApplicationAndPublishEvent() {

        LoanApplication application = LoanApplication.builder()
                .applicationId("APP1")
                .status(ApplicationStatus.APPLIED)
                .build();

        when(repository.findById("APP1"))
                .thenReturn(Optional.of(application));
        when(repository.save(any()))
                .thenReturn(application);

        LoanApplicationResponse response =
                service.review("APP1", true, "Approved", "ADMIN");

        assertEquals(ApplicationStatus.APPROVED, response.getStatus());

        verify(eventProducer)
                .publishApplicationApproved(any(LoanApplication.class));
    }

    // =========================
    // REJECT APPLICATION
    // =========================
    @Test
    void shouldRejectApplicationAndPublishEvent() {

        LoanApplication application = LoanApplication.builder()
                .applicationId("APP1")
                .status(ApplicationStatus.APPLIED)
                .build();

        when(repository.findById("APP1"))
                .thenReturn(Optional.of(application));
        when(repository.save(any()))
                .thenReturn(application);

        LoanApplicationResponse response =
                service.review("APP1", false, "Rejected", "ADMIN");

        assertEquals(ApplicationStatus.REJECTED, response.getStatus());

        verify(eventProducer)
                .publishApplicationRejected(any(LoanApplication.class));
    }

    // =========================
    // APPLICATION NOT FOUND
    // =========================
    @Test
    void shouldThrowExceptionWhenApplicationNotFound() {

        when(repository.findById("INVALID"))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.review("INVALID", true, "OK", "ADMIN")
        );

        assertEquals("Loan application not found", ex.getMessage());
    }
}

