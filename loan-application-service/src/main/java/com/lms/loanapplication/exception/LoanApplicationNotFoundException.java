package com.lms.loanapplication.exception;

public class LoanApplicationNotFoundException
        extends LoanApplicationException {

    public LoanApplicationNotFoundException(String id) {
        super("Loan application not found: " + id);
    }
}
