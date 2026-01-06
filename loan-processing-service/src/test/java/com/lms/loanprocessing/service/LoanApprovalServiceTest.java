package com.lms.loanprocessing.service;

import com.lms.loanprocessing.client.CustomerClient;
import com.lms.loanprocessing.event.LoanApplicationSubmittedEvent;
import com.lms.loanprocessing.model.Loan;
import com.lms.loanprocessing.model.enums.LoanStatus;
import com.lms.loanprocessing.model.enums.LoanType;
import com.lms.loanprocessing.repository.LoanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class LoanApprovalServiceTest {

    @Mock
    private CustomerClient customerClient;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private EmiScheduleService emiScheduleService;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private LoanApprovalService service;

    @Test
    void shouldApproveLoanWhenAllRulesPass() {

        LoanApplicationSubmittedEvent event =
                LoanApplicationSubmittedEvent.builder()
                        .applicationId("APP1")
                        .customerId("C1")
                        .loanType(LoanType.PERSONAL)
                        .requestedAmount(BigDecimal.valueOf(100000))
                        .tenureMonths(12)
                        .build();

        CustomerClient.CustomerProfile profile =
                new CustomerClient.CustomerProfile();
        profile.setCreditScore(750);
        profile.setMonthlyIncome(BigDecimal.valueOf(50000));
        profile.setExistingEmiLiability(BigDecimal.ZERO);
        profile.setEmail("test@mail.com");

        when(customerClient.getProfile("C1")).thenReturn(profile);
        when(loanRepository.existsByApplicationId("APP1")).thenReturn(false);
        when(loanRepository.existsByCustomerIdAndStatus("C1", LoanStatus.ACTIVE))
                .thenReturn(false);
        when(loanRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        service.process(event);

        verify(loanRepository).save(any(Loan.class));
        verify(emiScheduleService).generateSchedule(any());
        verify(kafkaTemplate).send(anyString(), any());
    }

    @Test
    void shouldRejectLoanWhenCreditScoreLow() {

        LoanApplicationSubmittedEvent event =
                LoanApplicationSubmittedEvent.builder()
                        .applicationId("APP2")
                        .customerId("C2")
                        .loanType(LoanType.PERSONAL)
                        .requestedAmount(BigDecimal.valueOf(50000))
                        .tenureMonths(12)
                        .build();

        CustomerClient.CustomerProfile profile =
                new CustomerClient.CustomerProfile();
        profile.setCreditScore(600);

        when(customerClient.getProfile("C2")).thenReturn(profile);
        when(loanRepository.existsByApplicationId("APP2")).thenReturn(false);

        service.process(event);

        verify(kafkaTemplate).send(anyString(), any());
        verify(loanRepository, never()).save(any());
    }
    @Test
    void shouldIgnoreDuplicateApplication() {

        LoanApplicationSubmittedEvent event =
                LoanApplicationSubmittedEvent.builder()
                        .applicationId("APP1")
                        .customerId("C1")
                        .build();

        when(loanRepository.existsByApplicationId("APP1"))
                .thenReturn(true);

        service.process(event);

        verifyNoInteractions(customerClient);
        verifyNoInteractions(kafkaTemplate);
    }
    @Test
    void shouldRejectWhenActiveLoanExists() {

        LoanApplicationSubmittedEvent event =
                LoanApplicationSubmittedEvent.builder()
                        .applicationId("APP3")
                        .customerId("C3")
                        .loanType(LoanType.PERSONAL)
                        .requestedAmount(BigDecimal.valueOf(50000))
                        .tenureMonths(12)
                        .build();

        CustomerClient.CustomerProfile profile =
                new CustomerClient.CustomerProfile();
        profile.setCreditScore(800);
        profile.setMonthlyIncome(BigDecimal.valueOf(60000));
        profile.setExistingEmiLiability(BigDecimal.ZERO);

        when(customerClient.getProfile("C3")).thenReturn(profile);
        when(loanRepository.existsByApplicationId("APP3")).thenReturn(false);
        when(loanRepository.existsByCustomerIdAndStatus("C3", LoanStatus.ACTIVE))
                .thenReturn(true);

        service.process(event);

        verify(kafkaTemplate).send(anyString(), any());
        verify(loanRepository, never()).save(any());
    }
    @Test
    void shouldRejectWhenEmiExceedsIncomeLimit() {

        LoanApplicationSubmittedEvent event =
                LoanApplicationSubmittedEvent.builder()
                        .applicationId("APP4")
                        .customerId("C4")
                        .loanType(LoanType.HOME)
                        .requestedAmount(BigDecimal.valueOf(1_000_000))
                        .tenureMonths(12)
                        .build();

        CustomerClient.CustomerProfile profile =
                new CustomerClient.CustomerProfile();
        profile.setCreditScore(800);
        profile.setMonthlyIncome(BigDecimal.valueOf(10000)); // low income
        profile.setExistingEmiLiability(BigDecimal.ZERO);

        when(customerClient.getProfile("C4")).thenReturn(profile);
        when(loanRepository.existsByApplicationId("APP4")).thenReturn(false);
        when(loanRepository.existsByCustomerIdAndStatus("C4", LoanStatus.ACTIVE))
                .thenReturn(false);

        service.process(event);

        verify(kafkaTemplate).send(anyString(), any());
    }



}
