package com.lms.loanapplication.exception;

import com.lms.loanapplication.model.enums.LoanType;

public class DuplicateLoanTypeException
        extends LoanApplicationException {

    public DuplicateLoanTypeException(LoanType loanType) {
        super("Active loan already exists for loan type: " + loanType);
    }
}
