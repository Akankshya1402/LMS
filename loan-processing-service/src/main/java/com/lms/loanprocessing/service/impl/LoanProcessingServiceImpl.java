package com.lms.loanprocessing.service.impl;

import com.lms.loanprocessing.event.LoanApprovedEvent;
import com.lms.loanprocessing.exception.EmiNotFoundException;
import com.lms.loanprocessing.exception.LoanNotFoundException;
import com.lms.loanprocessing.model.EmiSchedule;
import com.lms.loanprocessing.model.Loan;
import com.lms.loanprocessing.model.enums.EmiStatus;
import com.lms.loanprocessing.model.enums.LoanStatus;
import com.lms.loanprocessing.repository.EmiScheduleRepository;
import com.lms.loanprocessing.repository.LoanRepository;
import com.lms.loanprocessing.service.LoanProcessingService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LoanProcessingServiceImpl
        implements LoanProcessingService {

    private final LoanRepository loanRepository;
    private final EmiScheduleRepository emiRepository;

    @Override
    public void processApprovedApplication(LoanApprovedEvent event) {

        BigDecimal interestRate =
                BigDecimal.valueOf(event.getInterestRate());

        BigDecimal emi = calculateEmi(
                event.getApprovedAmount(),
                interestRate,
                event.getTenureMonths()
        );

        Loan loan = Loan.builder()
                .applicationId(event.getApplicationId())
                .customerId(event.getCustomerId())
                .principal(event.getApprovedAmount())
                .interestRate(interestRate)
                .tenureMonths(event.getTenureMonths())
                .emiAmount(emi)
                .outstandingAmount(
                        emi.multiply(
                                BigDecimal.valueOf(
                                        event.getTenureMonths())))
                .status(LoanStatus.ACTIVE)
                .disbursedAt(LocalDateTime.now())
                .build();

        Loan savedLoan = loanRepository.save(loan);
        generateEmiSchedule(savedLoan);
    }

    @Override
    public void recordEmiPayment(String loanId, Integer emiNumber) {

        EmiSchedule emi = emiRepository.findByLoanId(loanId)
                .stream()
                .filter(e -> e.getEmiNumber().equals(emiNumber))
                .findFirst()
                .orElseThrow(() ->
                        new EmiNotFoundException("EMI not found"));

        emi.setStatus(EmiStatus.PAID);
        emiRepository.save(emi);

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() ->
                        new LoanNotFoundException("Loan not found"));

        loan.setOutstandingAmount(
                loan.getOutstandingAmount()
                        .subtract(loan.getEmiAmount()));

        if (loan.getOutstandingAmount()
                .compareTo(BigDecimal.ZERO) <= 0) {

            loan.setStatus(LoanStatus.CLOSED);
            loan.setClosedAt(LocalDateTime.now());
        }

        loanRepository.save(loan);
    }

    @Override
    public Loan getLoan(String loanId) {
        return loanRepository.findById(loanId)
                .orElseThrow(() ->
                        new LoanNotFoundException("Loan not found"));
    }

    private void generateEmiSchedule(Loan loan) {

        for (int i = 1; i <= loan.getTenureMonths(); i++) {

            emiRepository.save(
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

    private BigDecimal calculateEmi(
            BigDecimal principal,
            BigDecimal rate,
            Integer months) {

        BigDecimal monthlyRate =
                rate.divide(BigDecimal.valueOf(1200),
                        10, RoundingMode.HALF_UP);

        BigDecimal numerator =
                principal.multiply(monthlyRate)
                        .multiply(
                                (BigDecimal.ONE.add(monthlyRate))
                                        .pow(months));

        BigDecimal denominator =
                (BigDecimal.ONE.add(monthlyRate))
                        .pow(months)
                        .subtract(BigDecimal.ONE);

        return numerator.divide(
                denominator,
                2,
                RoundingMode.HALF_UP);
    }
}
