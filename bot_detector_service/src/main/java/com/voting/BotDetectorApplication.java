package com.voting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Punto de entrada del servicio de detección de fraude y bots por IP. */
@SpringBootApplication
public class BotDetectorApplication {
    public static void main(String[] args) {
        SpringApplication.run(BotDetectorApplication.class, args);
    }
}