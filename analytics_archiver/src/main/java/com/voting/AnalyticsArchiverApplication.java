package com.voting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Punto de entrada del servicio de archivado y auditoría en PostgreSQL. */
@SpringBootApplication
public class AnalyticsArchiverApplication {
    public static void main(String[] args) {
        SpringApplication.run(AnalyticsArchiverApplication.class, args);
    }
}