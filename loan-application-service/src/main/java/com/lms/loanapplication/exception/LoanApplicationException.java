package com.lms.loanapplication.exception;

public abstract class LoanApplicationException
        extends RuntimeException {

    protected LoanApplicationException(String message) {
        super(message);
    }
}
