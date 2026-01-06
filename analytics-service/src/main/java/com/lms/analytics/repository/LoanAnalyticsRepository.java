package com.lms.analytics.repository;

import com.lms.analytics.model.LoanAnalytics;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LoanAnalyticsRepository
        extends MongoRepository<LoanAnalytics, String> {
}
