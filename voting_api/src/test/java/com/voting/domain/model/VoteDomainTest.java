package com.voting.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VoteDomainTest {

    @Test
    void givenBlankFields_whenCreatingValueObjects_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new VoterId(" "));
        assertThrows(IllegalArgumentException.class, () -> new CandidateId(null));
        assertThrows(IllegalArgumentException.class, () -> new RegionId(""));
        assertThrows(IllegalArgumentException.class, () -> new IpAddress("  "));
    }

    @Test
    void givenValidValueObjects_whenCastingVote_thenCreateImmutableVoteWithTimestamp() {
        // Arrange
        VoterId voterId = new VoterId("VOTER-100");
        CandidateId candidateId = new CandidateId("CAND-1");
        RegionId regionId = new RegionId("NORTE");
        IpAddress ipAddress = new IpAddress("10.0.0.1");

        // Act
        Vote vote = Vote.cast(voterId, candidateId, regionId, ipAddress);

        // Assert
        assertEquals(voterId, vote.voterId());
        assertEquals(candidateId, vote.candidateId());
        assertEquals(regionId, vote.regionId());
        assertEquals(ipAddress, vote.ipAddress());
        assertNotNull(vote.timestamp());
    }
}
