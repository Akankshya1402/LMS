package com.lms.customer.config;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
public class JwtConfig {

    @Value("${security.jwt.secret}")
    private String jwtSecret;

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(
                new SecretKeySpec(jwtSecret.getBytes(), "HmacSHA256")
        ).build();
    }
}

