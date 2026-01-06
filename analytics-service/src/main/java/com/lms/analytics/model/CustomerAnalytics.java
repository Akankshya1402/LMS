package com.lms.analytics.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "customer_analytics")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAnalytics {

    @Id
    private String id; // GLOBAL

    private long totalCustomers;
    private long verifiedCustomers;
    private long activeCustomers;
}
