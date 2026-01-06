package com.lms.loanprocessing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Profile("jmeter")
public class JMeterSecurityConfig {

    @Bean
    public SecurityFilterChain jmeterSecurityFilterChain(HttpSecurity http)
            throws Exception {

        http
            // Disable CSRF for performance testing
            .csrf(csrf -> csrf.disable())

            // Allow all requests without authentication
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )

            // No sessions needed for JMeter load tests
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

        return http.build();
    }
}
