package com.lms.payment.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ActuatorConfig {
    /*
     * Actuator endpoints are controlled via config-server.
     * This class exists to keep actuator-related customizations
     * centralized if needed later (health indicators, security).
     */
}
