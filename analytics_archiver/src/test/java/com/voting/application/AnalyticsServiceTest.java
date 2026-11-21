package com.voting.application;

import com.voting.infrastructure.persistence.VoteEntity;
import com.voting.infrastructure.persistence.VoteMapper;
import com.voting.infrastructure.persistence.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AnalyticsServiceTest {

    private VoteRepository voteRepositoryMock;
    private AnalyticsService analyticsService;

    @BeforeEach
    void setUp() {
        voteRepositoryMock = mock(VoteRepository.class);
        analyticsService = new AnalyticsService(voteRepositoryMock, new VoteMapper());
    }

    @Test
    void givenNewVote_whenArchiveVote_thenSaveNewEntity() {
        // Arrange
        Instant now = Instant.now();
        when(voteRepositoryMock.findByVoterId("VOTER-1")).thenReturn(Optional.empty());

        // Act
        analyticsService.archiveVote("VOTER-1", "CAND-A", "NORTE", now);

        // Assert
        verify(voteRepositoryMock, times(1)).save(any(VoteEntity.class));
    }

    @Test
    void givenExistingVoter_whenArchiveVote_thenUpdateExistingEntityWithLatestVote() {
        // Arrange
        Instant t1 = Instant.parse("2026-11-21T10:00:00Z");
        Instant t2 = Instant.parse("2026-11-21T10:05:00Z");
        VoteEntity existing = new VoteEntity("VOTER-1", "CAND-A", "NORTE", t1);
        when(voteRepositoryMock.findByVoterId("VOTER-1")).thenReturn(Optional.of(existing));

        // Act
        analyticsService.archiveVote("VOTER-1", "CAND-B", "SUR", t2);

        // Assert
        assertEquals("CAND-B", existing.getCandidateId());
        assertEquals("SUR", existing.getRegionId());
        assertEquals(t2, existing.getTimestamp());
        verify(voteRepositoryMock, times(1)).save(existing);
    }
}
