package com.lms.loanprocessing.service;

import com.lms.loanprocessing.dto.EmiOverviewResponse;
import com.lms.loanprocessing.exception.LoanNotFoundException;
import com.lms.loanprocessing.model.EmiSchedule;
import com.lms.loanprocessing.model.Loan;
import com.lms.loanprocessing.model.enums.EmiStatus;
import com.lms.loanprocessing.model.enums.LoanStatus;
import com.lms.loanprocessing.repository.EmiScheduleRepository;
import com.lms.loanprocessing.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanServicingService {

    private final LoanRepository loanRepository;
    private final EmiScheduleRepository emiRepository;

    // =========================
    // EVENT-DRIVEN: EMI PAID
    // =========================
    public void markEmiPaid(String loanId, Integer emiNumber) {

        EmiSchedule emi = emiRepository.findByLoanId(loanId)
                .stream()
                .filter(e -> e.getEmiNumber().equals(emiNumber))
                .findFirst()
                .orElseThrow(() ->
                        new LoanNotFoundException("EMI not found"));

        // Idempotency: ignore duplicate events
        if (emi.getStatus() == EmiStatus.PAID) {
            return;
        }

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

    // =========================
    // EMI OVERVIEW (READ-ONLY)
    // =========================
    public EmiOverviewResponse getEmiOverview(String loanId) {

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() ->
                        new LoanNotFoundException("Loan not found"));

        List<EmiSchedule> emis =
                emiRepository.findByLoanId(loanId);

        long paid =
                emis.stream()
                        .filter(e -> e.getStatus() == EmiStatus.PAID)
                        .count();

        long overdue =
                emis.stream()
                        .filter(e -> e.getStatus() == EmiStatus.OVERDUE)
                        .count();

        long pending =
                emis.stream()
                        .filter(e -> e.getStatus() == EmiStatus.PENDING)
                        .count();

        return EmiOverviewResponse.builder()
                .loanId(loanId)
                .totalEmis(emis.size())
                .paidEmis((int) paid)
                .pendingEmis((int) pending)
                .overdueEmis((int) overdue)
                .emiAmount(loan.getEmiAmount())
                .outstandingAmount(loan.getOutstandingAmount())
                .loanStatus(loan.getStatus())
                .build();
    }

    // =========================
    // SYSTEM JOB: MARK OVERDUE
    // =========================
    public void markOverdueEmis() {

        List<EmiSchedule> overdueCandidates =
                emiRepository.findAll()
                        .stream()
                        .filter(e ->
                                e.getStatus() == EmiStatus.PENDING &&
                                e.getDueDate().isBefore(LocalDate.now()))
                        .toList();

        for (EmiSchedule emi : overdueCandidates) {

            emi.setStatus(EmiStatus.OVERDUE);
            emiRepository.save(emi);

            Loan loan = loanRepository.findById(emi.getLoanId())
                    .orElseThrow(() ->
                            new LoanNotFoundException("Loan not found"));

            loan.setStatus(LoanStatus.DEFAULTED);
            loanRepository.save(loan);
        }
    }
}
