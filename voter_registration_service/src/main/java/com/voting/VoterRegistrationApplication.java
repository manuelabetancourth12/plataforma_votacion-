package com.voting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Punto de entrada del servicio de registro inicial de votantes elegibles. */
@SpringBootApplication
public class VoterRegistrationApplication {
    public static void main(String[] args) {
        SpringApplication.run(VoterRegistrationApplication.class, args);
    }
}
