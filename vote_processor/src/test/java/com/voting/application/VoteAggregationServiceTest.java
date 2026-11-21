package com.voting.application;

import com.voting.domain.LiveResultPublisher;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.mockito.Mockito.*;

class VoteAggregationServiceTest {

    @Test
    void givenProcessedVotes_whenBroadcastResults_thenPublishSnapshot() {
        // Arrange
        LiveResultPublisher publisherMock = mock(LiveResultPublisher.class);
        VoteAggregationService service = new VoteAggregationService(publisherMock);
        service.processNewVote("VOTER-1", "CAND-A");

        // Act
        service.broadcastResults();

        // Assert
        verify(publisherMock, times(1)).broadcast(1L, Map.of("CAND-A", 1L));
    }
}
