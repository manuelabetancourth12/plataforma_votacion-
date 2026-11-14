package com.voting.domain.model;

import java.time.Instant;

public record Vote(VoterId voterId, CandidateId candidateId, RegionId regionId, Instant timestamp) {
    public Vote {
        if (voterId == null || candidateId == null || regionId == null || timestamp == null) {
            throw new IllegalArgumentException("El voto está incompleto");
        }
    }
    
    public static Vote cast(VoterId voterId, CandidateId candidateId, RegionId regionId) {
        return new Vote(voterId, candidateId, regionId, Instant.now());
    }
}