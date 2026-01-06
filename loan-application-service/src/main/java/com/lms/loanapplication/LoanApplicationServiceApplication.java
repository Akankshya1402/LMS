package com.lms.loanapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.lms.loanapplication.client")
public class LoanApplicationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoanApplicationServiceApplication.class, args);
    }
}
