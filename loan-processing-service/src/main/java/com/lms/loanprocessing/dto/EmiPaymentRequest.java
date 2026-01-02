package com.lms.loanprocessing.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmiPaymentRequest {

    private String loanId;
    private Integer emiNumber;
}
