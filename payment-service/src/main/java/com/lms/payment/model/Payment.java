package com.lms.payment.model;

import com.lms.payment.model.enums.PaymentStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "payments")
@CompoundIndex(
        name = "unique_success_emi_payment",
        def = "{'loanId':1,'emiNumber':1,'status':1}",
        unique = true
)
public class Payment {

    @Id
    private String paymentId;

    private String loanId;
    private String customerId;

    private Integer emiNumber;
    private BigDecimal amount;

    private PaymentStatus status;
    private String transactionRef;
    private LocalDateTime createdAt;
}
