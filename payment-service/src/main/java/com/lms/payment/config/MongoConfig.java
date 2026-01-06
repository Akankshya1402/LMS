package com.lms.payment.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing
public class MongoConfig {
    /*
     * Mongo connection details come from Config Server.
     * Auditing enabled for future createdAt / updatedAt fields.
     */
}
