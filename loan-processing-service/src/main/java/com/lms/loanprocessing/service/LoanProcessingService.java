package com.lms.loanprocessing.service;

import com.lms.loanprocessing.event.LoanApprovedEvent;
import com.lms.loanprocessing.model.Loan;

public interface LoanProcessingService {

    void processApprovedApplication(LoanApprovedEvent event);

    void recordEmiPayment(String loanId, Integer emiNumber);

    Loan getLoan(String loanId);
}
