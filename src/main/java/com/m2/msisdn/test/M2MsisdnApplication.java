package com.m2.msisdn.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class M2MsisdnApplication {

    public static void main(String[] args) {
        SpringApplication.run(M2MsisdnApplication.class, args);
    }
}
