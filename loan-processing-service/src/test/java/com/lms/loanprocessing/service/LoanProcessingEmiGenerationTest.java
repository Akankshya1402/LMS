package com.lms.loanprocessing.service;

import com.lms.loanprocessing.event.LoanApprovedEvent;
import com.lms.loanprocessing.repository.EmiScheduleRepository;
import com.lms.loanprocessing.repository.LoanRepository;
import com.lms.loanprocessing.service.impl.LoanProcessingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanProcessingEmiGenerationTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private EmiScheduleRepository emiRepository;

    @InjectMocks
    private LoanProcessingServiceImpl service;

    @Test
    void shouldGenerateCorrectNumberOfEmis() {

        LoanApprovedEvent event = LoanApprovedEvent.builder()
                .applicationId("APP10")
                .customerId("C10")
                .approvedAmount(BigDecimal.valueOf(60000))
                .tenureMonths(6)
                .build();

        when(loanRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        service.processApprovedApplication(event);

        verify(emiRepository, times(6)).save(any());
    }
}
