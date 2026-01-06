package com.lms.loanapplication.exception;

public class KycNotVerifiedException
        extends LoanApplicationException {

    public KycNotVerifiedException() {
        super("KYC not verified. Cannot apply for loan.");
    }
}
