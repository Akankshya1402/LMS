package com.lms.loanapplication.repository;

import com.lms.loanapplication.model.LoanApplication;
import com.lms.loanapplication.model.enums.*;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LoanApplicationRepository
        extends MongoRepository<LoanApplication, String> {

    List<LoanApplication> findByCustomerId(String customerId);

    boolean existsByCustomerIdAndLoanType(
            String customerId,
            LoanType loanType
    );
}
