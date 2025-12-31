package com.lms.loanapplication.service.impl;

import com.lms.loanapplication.dto.LoanApplicationRequest;
import com.lms.loanapplication.dto.LoanApplicationResponse;
import com.lms.loanapplication.kafka.LoanApplicationEventProducer;
import com.lms.loanapplication.model.LoanApplication;
import com.lms.loanapplication.model.enums.ApplicationStatus;
import com.lms.loanapplication.repository.LoanApplicationRepository;
import com.lms.loanapplication.service.LoanApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanApplicationServiceImpl implements LoanApplicationService {

    private final LoanApplicationRepository repository;
    private final LoanApplicationEventProducer eventProducer;

    // =========================
    // CUSTOMER: APPLY FOR LOAN
    // =========================
    @Override
    public LoanApplicationResponse apply(
            String customerId,
            LoanApplicationRequest request) {

        LoanApplication application = LoanApplication.builder()
                .customerId(customerId)
                .loanType(request.getLoanType())
                .loanAmount(request.getLoanAmount())
                .tenureMonths(request.getTenureMonths())
                .monthlyIncome(request.getMonthlyIncome())
                .status(ApplicationStatus.APPLIED)
                .createdAt(LocalDateTime.now())
                .build();

        LoanApplication savedApplication = repository.save(application);

        // 🔥 Kafka event
        eventProducer.publishApplicationCreated(savedApplication);

        return mapToResponse(savedApplication);
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
    // ADMIN / LOAN OFFICER: VIEW PENDING
    // =========================
    @Override
    public List<LoanApplicationResponse> getPendingApplications() {

        return repository.findByStatus(ApplicationStatus.APPLIED)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================
    // ADMIN / LOAN OFFICER: REVIEW APPLICATION
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

        application.setStatus(
                approved ? ApplicationStatus.APPROVED
                         : ApplicationStatus.REJECTED);

        application.setRemarks(remarks);
        application.setReviewedBy(reviewer);
        application.setReviewedAt(LocalDateTime.now());

        LoanApplication savedApplication = repository.save(application);

        // 🔥 Kafka event based on decision
        if (approved) {
            eventProducer.publishApplicationApproved(savedApplication);
        } else {
            eventProducer.publishApplicationRejected(savedApplication);
        }

        return mapToResponse(savedApplication);
    }

    // =========================
    // ENTITY → RESPONSE MAPPER
    // =========================
    private LoanApplicationResponse mapToResponse(LoanApplication application) {

        LoanApplicationResponse response = new LoanApplicationResponse();
        response.setApplicationId(application.getApplicationId());
        response.setCustomerId(application.getCustomerId());
        response.setLoanType(application.getLoanType());
        response.setLoanAmount(application.getLoanAmount());
        response.setTenureMonths(application.getTenureMonths());
        response.setMonthlyIncome(application.getMonthlyIncome());
        response.setStatus(application.getStatus());
        response.setRemarks(application.getRemarks());
        response.setAppliedAt(application.getCreatedAt());

        return response;
    }
}

