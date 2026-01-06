package com.lms.loanprocessing.service;

import com.lms.loanprocessing.model.Loan;
import com.lms.loanprocessing.repository.EmiScheduleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmiScheduleServiceTest {

    @Mock
    private EmiScheduleRepository repository;

    @InjectMocks
    private EmiScheduleService service;

    @Test
    void shouldGenerateScheduleForAllMonths() {

        Loan loan = Loan.builder()
                .loanId("L1")
                .tenureMonths(3)
                .emiAmount(BigDecimal.valueOf(1000))
                .build();

        service.generateSchedule(loan);

        verify(repository, times(3)).save(any());
    }
}
