package com.lms.loanapplication.service.impl;

import com.lms.loanapplication.client.CustomerClient;
import com.lms.loanapplication.dto.*;
import com.lms.loanapplication.event.LoanApplicationSubmittedEvent;
import com.lms.loanapplication.kafka.LoanApplicationEventProducer;
import com.lms.loanapplication.model.*;
import com.lms.loanapplication.model.enums.ApplicationStatus;
import com.lms.loanapplication.repository.LoanApplicationRepository;
import com.lms.loanapplication.service.LoanApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class LoanApplicationServiceImpl
        implements LoanApplicationService {

    private final LoanApplicationRepository repository;
    private final CustomerClient customerClient;
    private final LoanApplicationEventProducer eventProducer;

    /** Active profile name */
    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    private static final Set<Integer> ALLOWED_TENURES =
            Set.of(12, 24, 36);

    @Override
    public LoanApplicationResponse apply(
            String customerId,
            LoanApplicationRequest request) {

        boolean isJmeter = "jmeter".equals(activeProfile);

        // 1️⃣ KYC + eligibility (SKIP for JMETER)
        if (!isJmeter) {
            customerClient.validateCustomerForLoan(customerId);
        }

        // 2️⃣ One active loan per type
        if (repository.existsByCustomerIdAndLoanType(
                customerId,
                request.getLoanType())) {

            throw new IllegalStateException(
                    "Active loan of this type already exists");
        }

        // 3️⃣ Tenure rule
        if (!ALLOWED_TENURES.contains(
                request.getTenureMonths())) {

            throw new IllegalArgumentException(
                    "Invalid tenure");
        }

        LoanApplication application =
                LoanApplication.builder()
                        .customerId(customerId)
                        .loanType(request.getLoanType())
                        .loanAmount(request.getLoanAmount())
                        .tenureMonths(request.getTenureMonths())
                        .status(ApplicationStatus.SUBMITTED)
                        .createdAt(LocalDateTime.now())
                        .build();

        LoanApplication saved =
                repository.save(application);

        // 4️⃣ Kafka event (SKIP for JMETER)
        if (!isJmeter) {
            eventProducer.publishSubmitted(saved);
        }

        return map(saved);
    }

    @Override
    public List<LoanApplicationResponse>
    getMyApplications(String customerId) {

        return repository.findByCustomerId(customerId)
                .stream()
                .map(this::map)
                .toList();
    }

    private LoanApplicationResponse map(LoanApplication app) {
        LoanApplicationResponse res =
                new LoanApplicationResponse();
        res.setApplicationId(app.getApplicationId());
        res.setCustomerId(app.getCustomerId());
        res.setLoanType(app.getLoanType());
        res.setLoanAmount(app.getLoanAmount());
        res.setTenureMonths(app.getTenureMonths());
        res.setStatus(app.getStatus());
        res.setAppliedAt(app.getCreatedAt());
        return res;
    }
}
