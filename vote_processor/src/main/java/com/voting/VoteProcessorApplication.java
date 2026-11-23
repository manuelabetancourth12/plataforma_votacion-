package com.voting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/** Punto de entrada del procesador global de votos en tiempo real. */
@SpringBootApplication
@EnableScheduling // Habilita la ejecución del @Scheduled cada 1 segundo
public class VoteProcessorApplication {
    public static void main(String[] args) {
        SpringApplication.run(VoteProcessorApplication.class, args);
    }
}