package com.lms.loanprocessing.service;

import com.lms.loanprocessing.dto.EmiOverviewResponse;
import com.lms.loanprocessing.model.EmiSchedule;
import com.lms.loanprocessing.model.Loan;
import com.lms.loanprocessing.model.enums.EmiStatus;
import com.lms.loanprocessing.model.enums.LoanStatus;
import com.lms.loanprocessing.repository.EmiScheduleRepository;
import com.lms.loanprocessing.repository.LoanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class LoanServicingServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private EmiScheduleRepository emiRepository;

    @InjectMocks
    private LoanServicingService service;

    @Test
    void shouldMarkEmiPaid() {

        EmiSchedule emi =
                EmiSchedule.builder()
                        .loanId("L1")
                        .emiNumber(1)
                        .status(EmiStatus.PENDING)
                        .build();

        Loan loan =
                Loan.builder()
                        .loanId("L1")
                        .emiAmount(BigDecimal.valueOf(1000))
                        .outstandingAmount(BigDecimal.valueOf(1000))
                        .status(LoanStatus.ACTIVE)
                        .build();

        when(emiRepository.findByLoanId("L1")).thenReturn(List.of(emi));
        when(loanRepository.findById("L1")).thenReturn(Optional.of(loan));

        service.markEmiPaid("L1", 1);

        assertEquals(EmiStatus.PAID, emi.getStatus());
        verify(loanRepository).save(any());
    }

    @Test
    void shouldReturnCorrectEmiOverview() {

        Loan loan =
                Loan.builder()
                        .loanId("L2")
                        .emiAmount(BigDecimal.valueOf(2000))
                        .outstandingAmount(BigDecimal.valueOf(8000))
                        .status(LoanStatus.ACTIVE)
                        .build();

        when(loanRepository.findById("L2"))
                .thenReturn(Optional.of(loan));

        when(emiRepository.findByLoanId("L2"))
                .thenReturn(List.of(
                        EmiSchedule.builder().status(EmiStatus.PAID).build(),
                        EmiSchedule.builder().status(EmiStatus.PENDING).build()
                ));

        EmiOverviewResponse overview =
                service.getEmiOverview("L2");

        assertEquals(1, overview.getPaidEmis());
        assertEquals(1, overview.getPendingEmis());
    }
    @Test
    void shouldIgnoreAlreadyPaidEmi() {

        EmiSchedule emi =
                EmiSchedule.builder()
                        .loanId("L3")
                        .emiNumber(1)
                        .status(EmiStatus.PAID)
                        .build();

        when(emiRepository.findByLoanId("L3"))
                .thenReturn(List.of(emi));

        service.markEmiPaid("L3", 1);

        verify(loanRepository, never()).save(any());
    }
    @Test
    void shouldCloseLoanWhenOutstandingBecomesZero() {

        EmiSchedule emi =
                EmiSchedule.builder()
                        .loanId("L4")
                        .emiNumber(1)
                        .status(EmiStatus.PENDING)
                        .build();

        Loan loan =
                Loan.builder()
                        .loanId("L4")
                        .emiAmount(BigDecimal.valueOf(1000))
                        .outstandingAmount(BigDecimal.valueOf(1000))
                        .status(LoanStatus.ACTIVE)
                        .build();

        when(emiRepository.findByLoanId("L4")).thenReturn(List.of(emi));
        when(loanRepository.findById("L4")).thenReturn(Optional.of(loan));

        service.markEmiPaid("L4", 1);

        assertEquals(LoanStatus.CLOSED, loan.getStatus());
    }
    @Test
    void shouldMarkOverdueEmisAndDefaultLoan() {

        EmiSchedule emi =
                EmiSchedule.builder()
                        .loanId("L5")
                        .status(EmiStatus.PENDING)
                        .dueDate(LocalDate.now().minusDays(1))
                        .build();

        Loan loan =
                Loan.builder()
                        .loanId("L5")
                        .status(LoanStatus.ACTIVE)
                        .build();

        when(emiRepository.findAll()).thenReturn(List.of(emi));
        when(loanRepository.findById("L5")).thenReturn(Optional.of(loan));

        service.markOverdueEmis();

        assertEquals(EmiStatus.OVERDUE, emi.getStatus());
        assertEquals(LoanStatus.DEFAULTED, loan.getStatus());
    }



}
