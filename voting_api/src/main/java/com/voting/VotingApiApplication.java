package com.voting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Punto de entrada principal del microservicio API de votación. */
@SpringBootApplication
public class VotingApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(VotingApiApplication.class, args);
    }
}