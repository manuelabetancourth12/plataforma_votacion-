package com.voting.domain.model;

import java.time.Instant;

/** Entidad de dominio inmutable que representa un voto emitido con su marca de tiempo. */
public record Vote(
        VoterId voterId,
        CandidateId candidateId,
        RegionId regionId,
        IpAddress ipAddress,
        Instant timestamp
) {
    public Vote {
        if (voterId == null || candidateId == null || regionId == null || ipAddress == null || timestamp == null) {
            throw new IllegalArgumentException("El voto está incompleto");
        }
    }

    public static Vote cast(VoterId voterId, CandidateId candidateId, RegionId regionId, IpAddress ipAddress) {
        return new Vote(voterId, candidateId, regionId, ipAddress, Instant.now());
    }
}
