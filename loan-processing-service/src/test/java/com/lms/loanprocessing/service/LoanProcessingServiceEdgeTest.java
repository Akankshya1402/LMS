package com.lms.loanprocessing.service;

import com.lms.loanprocessing.exception.EmiNotFoundException;
import com.lms.loanprocessing.exception.LoanNotFoundException;
import com.lms.loanprocessing.model.EmiSchedule;
import com.lms.loanprocessing.model.Loan;
import com.lms.loanprocessing.model.enums.EmiStatus;
import com.lms.loanprocessing.model.enums.LoanStatus;
import com.lms.loanprocessing.repository.EmiScheduleRepository;
import com.lms.loanprocessing.repository.LoanRepository;
import com.lms.loanprocessing.service.impl.LoanProcessingServiceImpl;
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
class LoanProcessingServiceEdgeTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private EmiScheduleRepository emiRepository;

    @InjectMocks
    private LoanProcessingServiceImpl service;

    // =========================
    // EMI NOT FOUND
    // =========================
    @Test
    void shouldThrowExceptionWhenEmiNotFound() {

        when(emiRepository.findByLoanId("L1"))
                .thenReturn(List.of());

        EmiNotFoundException ex = assertThrows(
                EmiNotFoundException.class,
                () -> service.recordEmiPayment("L1", 99)
        );

        assertEquals("EMI not found", ex.getMessage());
    }

    // =========================
    // LOAN NOT FOUND
    // =========================
    @Test
    void shouldThrowExceptionWhenLoanNotFound() {

        EmiSchedule emi = EmiSchedule.builder()
                .emiNumber(1)
                .status(EmiStatus.PENDING)
                .build();

        when(emiRepository.findByLoanId("L1"))
                .thenReturn(List.of(emi));

        when(loanRepository.findById("L1"))
                .thenReturn(Optional.empty());

        LoanNotFoundException ex = assertThrows(
                LoanNotFoundException.class,
                () -> service.recordEmiPayment("L1", 1)
        );

        assertEquals("Loan not found", ex.getMessage());
    }

    // =========================
    // LOAN STILL ACTIVE
    // =========================
    @Test
    void shouldKeepLoanActiveIfOutstandingRemaining() {

        Loan loan = Loan.builder()
                .loanId("L1")
                .emiAmount(BigDecimal.valueOf(1000))
                .outstandingAmount(BigDecimal.valueOf(5000))
                .status(LoanStatus.ACTIVE)
                .build();

        EmiSchedule emi = EmiSchedule.builder()
                .emiNumber(1)
                .status(EmiStatus.PENDING)
                .build();

        when(emiRepository.findByLoanId("L1"))
                .thenReturn(List.of(emi));

        when(loanRepository.findById("L1"))
                .thenReturn(Optional.of(loan));

        service.recordEmiPayment("L1", 1);

        assertEquals(LoanStatus.ACTIVE, loan.getStatus());
        verify(loanRepository).save(loan);
    }
}
