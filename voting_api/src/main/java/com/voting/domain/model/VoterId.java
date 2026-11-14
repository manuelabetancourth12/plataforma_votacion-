package com.voting.domain.model;

public record VoterId(String value) {
    public VoterId {
        if (value == null || value.trim().isBlank()) {
            throw new IllegalArgumentException("VoterId no puede ser nulo ni vacío");
        }
    }
}