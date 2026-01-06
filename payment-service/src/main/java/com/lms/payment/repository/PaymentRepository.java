package com.lms.payment.repository;

import com.lms.payment.model.Payment;
import com.lms.payment.model.enums.PaymentStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PaymentRepository extends MongoRepository<Payment, String> {

    List<Payment> findByLoanId(String loanId);

    List<Payment> findByCustomerId(String customerId);

    boolean existsByLoanIdAndEmiNumberAndStatus(
            String loanId,
            Integer emiNumber,
            PaymentStatus status
    );
}
