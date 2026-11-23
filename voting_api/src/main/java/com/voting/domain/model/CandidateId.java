package com.voting.domain.model;

/** Value Object que garantiza un identificador de candidato no nulo ni vacío. */
public record CandidateId(String value) {
    public CandidateId {
        if (value == null || value.trim().isBlank()) {
            throw new IllegalArgumentException("CandidateId no puede ser nulo");
        }
    }
}