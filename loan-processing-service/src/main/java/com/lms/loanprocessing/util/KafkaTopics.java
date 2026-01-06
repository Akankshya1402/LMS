package com.lms.loanprocessing.util;

public final class KafkaTopics {

    private KafkaTopics() {
        // utility class
    }

    public static final String LOAN_APPLICATION_SUBMITTED =
            "loan-application-submitted";

    public static final String LOAN_APPROVED =
            "loan-approved";

    public static final String LOAN_REJECTED =
            "loan-rejected";

    public static final String EMI_PAID =
            "emi-paid";
}
