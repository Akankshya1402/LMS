package com.lms.loanprocessing.repository;

import com.lms.loanprocessing.model.Loan;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LoanRepository
        extends MongoRepository<Loan, String> {

    Loan findByApplicationId(String applicationId);
}
