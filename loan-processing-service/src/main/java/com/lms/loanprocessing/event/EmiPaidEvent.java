package com.lms.loanprocessing.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmiPaidEvent {

    private String loanId;

    private Integer emiNumber;

    private BigDecimal amount;

    private LocalDateTime paidAt;
}
