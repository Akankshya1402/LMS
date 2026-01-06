package com.lms.analytics.repository;

import com.lms.analytics.model.PaymentAnalytics;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentAnalyticsRepository
        extends MongoRepository<PaymentAnalytics, String> {
}
