package com.lms.loanprocessing.service;

import com.lms.loanprocessing.model.*;
import com.lms.loanprocessing.model.enums.EmiStatus;
import com.lms.loanprocessing.repository.EmiScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class EmiScheduleService {

    private final EmiScheduleRepository repository;

    public void generateSchedule(Loan loan) {

        for (int i = 1; i <= loan.getTenureMonths(); i++) {
            repository.save(
                    EmiSchedule.builder()
                            .loanId(loan.getLoanId())
                            .emiNumber(i)
                            .emiAmount(loan.getEmiAmount())
                            .dueDate(LocalDate.now().plusMonths(i))
                            .status(EmiStatus.PENDING)
                            .build()
            );
        }
    }
}
