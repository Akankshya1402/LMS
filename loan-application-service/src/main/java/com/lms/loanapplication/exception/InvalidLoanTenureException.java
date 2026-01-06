package com.lms.loanapplication.exception;

public class InvalidLoanTenureException
        extends LoanApplicationException {

    public InvalidLoanTenureException(Integer tenure) {
        super("Invalid loan tenure: " + tenure);
    }
}
