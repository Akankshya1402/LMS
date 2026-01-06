package com.lms.loanprocessing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.lms.loanprocessing.client")
public class LoanProcessingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoanProcessingServiceApplication.class, args);
    }
}

