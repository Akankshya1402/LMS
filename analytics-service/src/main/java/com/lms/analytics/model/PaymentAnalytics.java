package com.lms.analytics.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "payment_analytics")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentAnalytics {

    @Id
    private String id; // GLOBAL

    private BigDecimal totalCollected;
    private BigDecimal totalPending;

    private long paidEmis;
    private long pendingEmis;
}

