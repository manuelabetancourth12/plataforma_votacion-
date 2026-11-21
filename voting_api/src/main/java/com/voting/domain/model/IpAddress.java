package com.voting.domain.model;

public record IpAddress(String value) {
    public IpAddress {
        if (value == null || value.trim().isBlank()) {
            throw new IllegalArgumentException("IpAddress no puede ser nula ni vacía");
        }
    }
}
