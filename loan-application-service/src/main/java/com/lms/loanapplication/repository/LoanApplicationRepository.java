package com.lms.loanapplication.repository;

import com.lms.loanapplication.model.LoanApplication;
import com.lms.loanapplication.model.enums.ApplicationStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.lms.loanapplication.model.enums.LoanType;

import java.util.List;

public interface LoanApplicationRepository
        extends MongoRepository<LoanApplication, String> {

    List<LoanApplication> findByCustomerId(String customerId);

    List<LoanApplication> findByStatus(ApplicationStatus status);

    // RULE: only one approved loan per type
    boolean existsByCustomerIdAndLoanTypeAndStatus(
            String customerId,
            LoanType loanType,
            ApplicationStatus status
    );
}
