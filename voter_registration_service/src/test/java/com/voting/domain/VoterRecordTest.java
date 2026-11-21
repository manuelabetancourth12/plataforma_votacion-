package com.voting.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VoterRecordTest {

    @Test
    void givenBlankVoterId_whenCreateVoterRecord_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new VoterRecord(" ", true));
        assertThrows(IllegalArgumentException.class, () -> new VoterRecord(null, true));
    }

    @Test
    void givenValidVoterId_whenCreateVoterRecord_thenPreserveFields() {
        // Arrange & Act
        VoterRecord record = new VoterRecord("VOTER-100", true);

        // Assert
        assertEquals("VOTER-100", record.voterId());
        assertTrue(record.eligible());
    }
}
