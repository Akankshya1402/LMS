package com.lms.loanapplication.service.impl;

import com.lms.loanapplication.client.CustomerClient;
import com.lms.loanapplication.dto.LoanApplicationRequest;
import com.lms.loanapplication.dto.LoanApplicationResponse;
import com.lms.loanapplication.kafka.LoanApplicationEventProducer;
import com.lms.loanapplication.model.LoanApplication;
import com.lms.loanapplication.model.enums.ApplicationStatus;
import com.lms.loanapplication.repository.LoanApplicationRepository;
import com.lms.loanapplication.service.LoanApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class LoanApplicationServiceImpl implements LoanApplicationService {

    private final LoanApplicationRepository repository;
    private final LoanApplicationEventProducer eventProducer;
    private final CustomerClient customerClient;

    private static final BigDecimal MIN_AMOUNT = BigDecimal.valueOf(10_000);
    private static final BigDecimal MAX_AMOUNT = BigDecimal.valueOf(10_00_000);
    private static final Set<Integer> ALLOWED_TENURES = Set.of(12, 24, 36);
    private static final int MIN_CREDIT_SCORE = 650;

    // =========================
    // CUSTOMER: APPLY FOR LOAN
    // =========================
    @Override
    public LoanApplicationResponse apply(
            String customerId,
            LoanApplicationRequest request) {

        // 1️⃣ Customer must be ACTIVE + KYC VERIFIED
        customerClient.validateCustomerForLoan(customerId);

        // 2️⃣ Only ONE approved loan per loan type
        if (repository.existsByCustomerIdAndLoanTypeAndStatus(
                customerId,
                request.getLoanType(),
                ApplicationStatus.APPROVED)) {

            throw new IllegalStateException(
                    "Active " + request.getLoanType() + " loan already exists");
        }

        // 3️⃣ Loan amount rule
        if (request.getLoanAmount().compareTo(MIN_AMOUNT) < 0 ||
            request.getLoanAmount().compareTo(MAX_AMOUNT) > 0) {

            throw new IllegalArgumentException("Invalid loan amount");
        }

        // 4️⃣ Tenure rule
        if (!ALLOWED_TENURES.contains(request.getTenureMonths())) {
            throw new IllegalArgumentException("Invalid loan tenure");
        }

        LoanApplication application = LoanApplication.builder()
                .customerId(customerId)
                .loanType(request.getLoanType())
                .loanAmount(request.getLoanAmount())
                .tenureMonths(request.getTenureMonths())
                .monthlyIncome(request.getMonthlyIncome())
                .status(ApplicationStatus.APPLIED)
                .createdAt(LocalDateTime.now())
                .build();

        LoanApplication saved = repository.save(application);
        eventProducer.publishApplicationCreated(saved);

        return mapToResponse(saved);
    }

    // =========================
    // CUSTOMER: VIEW OWN APPLICATIONS
    // =========================
    @Override
    public List<LoanApplicationResponse> getMyApplications(String customerId) {
        return repository.findByCustomerId(customerId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================
    // LOAN OFFICER: VIEW PENDING
    // =========================
    @Override
    public List<LoanApplicationResponse> getPendingApplications() {
        return repository.findByStatus(ApplicationStatus.APPLIED)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================
    // LOAN OFFICER: REVIEW
    // =========================
    @Override
    public LoanApplicationResponse review(
            String applicationId,
            boolean approved,
            String remarks,
            String reviewer) {

        LoanApplication application = repository.findById(applicationId)
                .orElseThrow(() ->
                        new RuntimeException("Loan application not found"));

        // 🔒 Prevent double review
        if (application.getStatus() != ApplicationStatus.APPLIED) {
            throw new IllegalStateException(
                    "Loan application already reviewed");
        }

        application.setStatus(ApplicationStatus.UNDER_REVIEW);

        if (!approved && (remarks == null || remarks.isBlank())) {
            throw new IllegalArgumentException("Rejection reason is mandatory");
        }

        if (approved) {

            Integer creditScore =
                    customerClient.getCreditScore(application.getCustomerId());

            if (creditScore < MIN_CREDIT_SCORE) {
                throw new IllegalStateException("Credit score too low");
            }

            BigDecimal emi =
                    application.getLoanAmount()
                            .divide(
                                    BigDecimal.valueOf(application.getTenureMonths()),
                                    2,
                                    RoundingMode.HALF_UP
                            );

            customerClient.updateEmiLiability(
                    application.getCustomerId(),
                    emi
            );

            application.setStatus(ApplicationStatus.APPROVED);
            eventProducer.publishApplicationApproved(application);

        } else {
            application.setStatus(ApplicationStatus.REJECTED);
            application.setRemarks(remarks);
            eventProducer.publishApplicationRejected(application);
        }

        application.setReviewedBy(reviewer);
        application.setReviewedAt(LocalDateTime.now());

        return mapToResponse(repository.save(application));
    }

    // =========================
    // ENTITY → RESPONSE
    // =========================
    private LoanApplicationResponse mapToResponse(LoanApplication app) {

        LoanApplicationResponse response = new LoanApplicationResponse();
        response.setApplicationId(app.getApplicationId());
        response.setCustomerId(app.getCustomerId());
        response.setLoanType(app.getLoanType());
        response.setLoanAmount(app.getLoanAmount());
        response.setTenureMonths(app.getTenureMonths());
        response.setMonthlyIncome(app.getMonthlyIncome());
        response.setStatus(app.getStatus());
        response.setRemarks(app.getRemarks());
        response.setAppliedAt(app.getCreatedAt());

        return response;
    }
}

