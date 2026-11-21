package com.voting.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IpActivityContextTest {

    @Test
    void givenIpActivity_whenExceedingThresholds_thenTransitionThroughStates() {
        // Arrange
        IpActivityContext context = new IpActivityContext("192.168.1.50");

        // Act & Assert: 1-3 votos -> NORMAL
        for (int i = 0; i < 3; i++) {
            context.registerVote();
        }
        assertEquals("NORMAL", context.getCurrentRiskLevel());

        // Act & Assert: 4-5 votos -> SOSPECHOSA
        context.registerVote();
        assertEquals("SOSPECHOSA", context.getCurrentRiskLevel());

        context.registerVote();
        assertEquals("SOSPECHOSA", context.getCurrentRiskLevel());

        // Act & Assert: >5 votos -> BLOQUEADA (terminal)
        context.registerVote();
        assertEquals("BLOQUEADA", context.getCurrentRiskLevel());

        context.registerVote();
        assertEquals("BLOQUEADA", context.getCurrentRiskLevel());
    }
}
