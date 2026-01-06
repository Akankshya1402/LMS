package com.lms.loanprocessing.repository;

import com.lms.loanprocessing.model.Loan;
import com.lms.loanprocessing.model.enums.LoanStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LoanRepository
        extends MongoRepository<Loan, String> {

    // 🔒 Idempotency check (Kafka safety)
    boolean existsByApplicationId(String applicationId);

    // 🔒 Business rule: only one ACTIVE loan per customer
    boolean existsByCustomerIdAndStatus(
            String customerId,
            LoanStatus status
    );
}
