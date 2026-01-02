package com.lms.notification.config;

public final class KafkaTopics {

    private KafkaTopics() {}

    public static final String LOAN_APPROVED = "loan-approved";
    public static final String EMI_DUE = "emi-due";
    public static final String LOAN_CLOSED = "loan-closed";
}
