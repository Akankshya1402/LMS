package com.lms.notification.dto;

import lombok.Data;

@Data
public class LoanApprovedEvent {
    private String applicationId;
    private String customerEmail;
    private Double loanAmount;
}
