package com.lms.loanprocessing.event;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanRejectedEvent {

    private String applicationId;
    private String customerId;
    private String rejectionReason;
}
