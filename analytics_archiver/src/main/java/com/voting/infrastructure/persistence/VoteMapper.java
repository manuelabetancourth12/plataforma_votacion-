package com.voting.infrastructure.persistence;

import org.springframework.stereotype.Component;

import java.time.Instant;

/** Mapper que valida los campos del evento y construye instancias de VoteEntity. */
@Component
public class VoteMapper {

    public VoteEntity toEntity(String voterId, String candidateId, String regionId, Instant timestamp) {
        if (voterId == null || voterId.isBlank()
                || candidateId == null || candidateId.isBlank()
                || regionId == null || regionId.isBlank()
                || timestamp == null) {
            throw new IllegalArgumentException("El evento de voto contiene campos inválidos");
        }
        return new VoteEntity(voterId, candidateId, regionId, timestamp);
    }
}
