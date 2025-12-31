package com.lms.loanapplication.kafka;

public final class KafkaTopics {

    private KafkaTopics() {}

    public static final String LOAN_APP_CREATED =
            "loan-application-created";

    public static final String LOAN_APP_APPROVED =
            "loan-application-approved";

    public static final String LOAN_APP_REJECTED =
            "loan-application-rejected";
}
