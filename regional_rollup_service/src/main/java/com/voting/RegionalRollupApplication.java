package com.voting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/** Punto de entrada del servicio de agregación regional ponderada. */
@SpringBootApplication
@EnableScheduling
public class RegionalRollupApplication {
    public static void main(String[] args) {
        SpringApplication.run(RegionalRollupApplication.class, args);
    }
}
