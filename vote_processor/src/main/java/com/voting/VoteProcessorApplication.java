package com.voting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // Habilita la ejecución del @Scheduled cada 1 segundo
public class VoteProcessorApplication {
    public static void main(String[] args) {
        SpringApplication.run(VoteProcessorApplication.class, args);
    }
}