package com.lms.analytics.repository;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@TestConfiguration
public class TestMongoConfig {

    @Bean
    MongoTemplate mongoTemplate() {
        return new MongoTemplate(
                new SimpleMongoClientDatabaseFactory(
                        "mongodb://localhost:27017/test-db"
                )
        );
    }
}
