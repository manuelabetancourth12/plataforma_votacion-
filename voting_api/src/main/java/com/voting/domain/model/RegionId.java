package com.voting.domain.model;

public record RegionId(String value) {
    public RegionId {
        if (value == null || value.trim().isBlank()) {
            throw new IllegalArgumentException("RegionId no puede ser nulo");
        }
    }
}