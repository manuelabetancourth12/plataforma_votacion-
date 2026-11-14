package com.voting.domain.model;

public record CandidateId(String value) {
    public CandidateId {
        if (value == null || value.trim().isBlank()) {
            throw new IllegalArgumentException("CandidateId no puede ser nulo");
        }
    }
}