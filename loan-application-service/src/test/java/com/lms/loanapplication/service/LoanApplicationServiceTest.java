package com.lms.loanapplication.service;

import com.lms.loanapplication.client.CustomerClient;
import com.lms.loanapplication.dto.*;
import com.lms.loanapplication.kafka.LoanApplicationEventProducer;
import com.lms.loanapplication.model.*;
import com.lms.loanapplication.model.enums.*;
import com.lms.loanapplication.repository.LoanApplicationRepository;
import com.lms.loanapplication.service.impl.LoanApplicationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

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

    @Test
    void shouldApplyForLoanAndPublishEvent() {

        LoanApplicationRequest request = new LoanApplicationRequest();
        request.setLoanType(LoanType.PERSONAL);
        request.setLoanAmount(BigDecimal.valueOf(200000));
        request.setTenureMonths(24);

        LoanApplication saved = LoanApplication.builder()
                .applicationId("APP1")
                .customerId("CUST1")
                .loanType(LoanType.PERSONAL)
                .loanAmount(request.getLoanAmount())
                .tenureMonths(24)
                .status(ApplicationStatus.SUBMITTED)
                .build();

        doNothing().when(customerClient)
                .validateCustomerForLoan("CUST1");

        when(repository.existsByCustomerIdAndLoanType(
                any(), any())).thenReturn(false);

        when(repository.save(any())).thenReturn(saved);

        LoanApplicationResponse response =
                service.apply("CUST1", request);

        assertEquals("APP1", response.getApplicationId());
        assertEquals(ApplicationStatus.SUBMITTED, response.getStatus());

        verify(eventProducer).publishSubmitted(any());
        verify(repository).save(any());
    }

    @Test
    void shouldReturnCustomerApplications() {

        when(repository.findByCustomerId("C1"))
                .thenReturn(List.of(
                        LoanApplication.builder()
                                .applicationId("APP1")
                                .status(ApplicationStatus.SUBMITTED)
                                .build()
                ));

        List<LoanApplicationResponse> responses =
                service.getMyApplications("C1");

        assertEquals(1, responses.size());
        verify(repository).findByCustomerId("C1");
    }
}

