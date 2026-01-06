package com.lms.analytics.repository;

import com.lms.analytics.model.CustomerAnalytics;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerAnalyticsRepository
        extends MongoRepository<CustomerAnalytics, String> {
}

