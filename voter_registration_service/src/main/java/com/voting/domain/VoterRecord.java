package com.voting.domain;

/** Record de dominio que representa un registro del padrón y valida su voterId. */
public record VoterRecord(String voterId, boolean eligible) {
    public VoterRecord {
        if (voterId == null || voterId.trim().isBlank()) {
            throw new IllegalArgumentException("El voterId no puede ser nulo ni vacío");
        }
    }
}
