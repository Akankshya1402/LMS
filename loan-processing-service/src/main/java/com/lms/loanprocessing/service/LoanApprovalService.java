package com.lms.loanprocessing.service;

import com.lms.loanprocessing.client.CustomerClient;
import com.lms.loanprocessing.event.*;
import com.lms.loanprocessing.model.Loan;
import com.lms.loanprocessing.model.enums.LoanStatus;
import com.lms.loanprocessing.model.enums.LoanType;
import com.lms.loanprocessing.repository.LoanRepository;
import com.lms.loanprocessing.util.EmiCalculator;
import com.lms.loanprocessing.util.KafkaTopics;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LoanApprovalService {

    private static final int MIN_CREDIT_SCORE = 650;
    private static final BigDecimal EMI_RATIO = BigDecimal.valueOf(0.40);

    private final CustomerClient customerClient;
    private final LoanRepository loanRepository;
    private final EmiScheduleService emiService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void process(LoanApplicationSubmittedEvent event) {

        // 🔒 Idempotency check
        if (loanRepository.existsByApplicationId(event.getApplicationId())) {
            return;
        }

        CustomerClient.CustomerProfile customer =
                customerClient.getProfile(event.getCustomerId());

        if (customer.getCreditScore() < MIN_CREDIT_SCORE) {
            reject(event, "Credit score below 650");
            return;
        }

        // 🔒 Only one active loan rule
        if (loanRepository.existsByCustomerIdAndStatus(
                event.getCustomerId(),
                LoanStatus.ACTIVE)) {

            reject(event, "Active loan already exists");
            return;
        }

        BigDecimal rate = interestRate(event.getLoanType());
        BigDecimal emi =
                EmiCalculator.calculate(
                        event.getRequestedAmount(),
                        rate,
                        event.getTenureMonths());

        BigDecimal maxAllowed =
                customer.getMonthlyIncome().multiply(EMI_RATIO);

        if (customer.getExistingEmiLiability()
                .add(emi)
                .compareTo(maxAllowed) > 0) {

            reject(event, "EMI exceeds 40% of income");
            return;
        }

        Loan loan =
                loanRepository.save(
                        Loan.builder()
                                .applicationId(event.getApplicationId())
                                .customerId(event.getCustomerId())
                                .principal(event.getRequestedAmount())
                                .interestRate(rate)
                                .tenureMonths(event.getTenureMonths())
                                .emiAmount(emi)
                                .outstandingAmount(
                                        emi.multiply(
                                                BigDecimal.valueOf(
                                                        event.getTenureMonths())))
                                .status(LoanStatus.ACTIVE)
                                .disbursedAt(LocalDateTime.now())
                                .build());

        emiService.generateSchedule(loan);

        customerClient.updateEmiLiability(
                loan.getCustomerId(),
                emi);

        kafkaTemplate.send(
                KafkaTopics.LOAN_APPROVED,
                LoanApprovedEvent.builder()
                        .applicationId(event.getApplicationId())
                        .customerId(event.getCustomerId())
                        .customerEmail(customer.getEmail())
                        .approvedAmount(event.getRequestedAmount())
                        .tenureMonths(event.getTenureMonths())
                        .interestRate(rate.doubleValue())
                        .build());
    }

    private void reject(
            LoanApplicationSubmittedEvent event,
            String reason) {

        kafkaTemplate.send(
                KafkaTopics.LOAN_REJECTED,
                LoanRejectedEvent.builder()
                        .applicationId(event.getApplicationId())
                        .customerId(event.getCustomerId())
                        .rejectionReason(reason)
                        .build());
    }

    private BigDecimal interestRate(LoanType type) {
        return switch (type) {
            case HOME -> BigDecimal.valueOf(7.5);
            case VEHICLE -> BigDecimal.valueOf(9.0);
            default -> BigDecimal.valueOf(8.5);
        };
    }
}
