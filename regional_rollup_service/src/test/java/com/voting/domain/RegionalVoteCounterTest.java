package com.voting.domain;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RegionalVoteCounterTest {

    @Test
    void givenCustomWeightStrategy_whenRegisterVoteAndChangeVote_thenApplyWeightsAndUniqueness() {
        // Arrange
        RegionWeightStrategy strategy = new CustomRegionWeightStrategy(Map.of("NORTE", 2.0, "SUR", 1.5));
        RegionalVoteCounter counter = new RegionalVoteCounter(strategy);

        // Act
        counter.registerVote("VOTER-1", "NORTE", "CAND-A");
        counter.registerVote("VOTER-1", "NORTE", "CAND-B");
        counter.registerVote("VOTER-2", "SUR", "CAND-A");

        // Assert
        Map<String, Map<String, Double>> snapshot = counter.getSnapshot();
        assertEquals(0.0, snapshot.get("norte").get("CAND-A"));
        assertEquals(2.0, snapshot.get("norte").get("CAND-B"));
        assertEquals(1.5, snapshot.get("sur").get("CAND-A"));
    }
}
