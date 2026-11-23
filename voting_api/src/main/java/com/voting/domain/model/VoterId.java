package com.voting.domain.model;

/** Value Object que garantiza un identificador de votante no nulo ni vacío. */
public record VoterId(String value) {
    public VoterId {
        if (value == null || value.trim().isBlank()) {
            throw new IllegalArgumentException("VoterId no puede ser nulo ni vacío");
        }
    }
}