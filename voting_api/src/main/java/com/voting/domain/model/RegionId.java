package com.voting.domain.model;

/** Value Object que garantiza un identificador de región no nulo ni vacío. */
public record RegionId(String value) {
    public RegionId {
        if (value == null || value.trim().isBlank()) {
            throw new IllegalArgumentException("RegionId no puede ser nulo");
        }
    }
}