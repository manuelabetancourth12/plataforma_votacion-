package com.voting.domain;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalVoteCounterTest {

    @Test
    void givenNewVoters_whenRegisterVotes_thenCountAccurately() {
        // Arrange
        GlobalVoteCounter counter = new GlobalVoteCounter();

        // Act
        counter.registerVote("VOTER-1", "CAND-A");
        counter.registerVote("VOTER-2", "CAND-B");

        // Assert
        assertEquals(2L, counter.getTotalVotes());
        Map<String, Long> snapshot = counter.getSnapshot();
        assertEquals(1L, snapshot.get("CAND-A"));
        assertEquals(1L, snapshot.get("CAND-B"));
    }

    @Test
    void givenSameVoterChangesCandidate_whenRegisterVote_thenKeepLastVoteOnly() {
        // Arrange
        GlobalVoteCounter counter = new GlobalVoteCounter();
        counter.registerVote("VOTER-1", "CAND-A");

        // Act
        counter.registerVote("VOTER-1", "CAND-B");
        counter.registerVote("VOTER-1", "CAND-B"); // idempotente

        // Assert
        assertEquals(1L, counter.getTotalVotes());
        Map<String, Long> snapshot = counter.getSnapshot();
        assertEquals(0L, snapshot.get("CAND-A"));
        assertEquals(1L, snapshot.get("CAND-B"));
    }
}
