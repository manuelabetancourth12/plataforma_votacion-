package com.voting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Punto de entrada del worker WebSocket para resultados globales en vivo. */
@SpringBootApplication
public class GlobalDashboardApplication {
    public static void main(String[] args) {
        SpringApplication.run(GlobalDashboardApplication.class, args);
    }
}
