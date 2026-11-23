package com.voting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Punto de entrada del worker WebSocket para resultados regionales en vivo. */
@SpringBootApplication
public class RegionalDashboardApplication {
    public static void main(String[] args) {
        SpringApplication.run(RegionalDashboardApplication.class, args);
    }
}
