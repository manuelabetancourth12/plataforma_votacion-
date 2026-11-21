package com.voting.application;

import com.voting.domain.RegionalResultPublisher;
import com.voting.domain.UniformWeightStrategy;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.mockito.Mockito.*;

class RegionalRollupServiceTest {

    @Test
    void givenRegionalVotes_whenBroadcastRegionalResults_thenPublishPerRegion() {
        // Arrange
        RegionalResultPublisher publisherMock = mock(RegionalResultPublisher.class);
        RegionalRollupService service = new RegionalRollupService(new UniformWeightStrategy(), publisherMock);
        service.processVote("VOTER-1", "NORTE", "CAND-A");
        service.processVote("VOTER-2", "SUR", "CAND-B");

        // Act
        service.broadcastRegionalResults();

        // Assert
        verify(publisherMock, times(1)).publishRegionResults("norte", Map.of("CAND-A", 1.0));
        verify(publisherMock, times(1)).publishRegionResults("sur", Map.of("CAND-B", 1.0));
    }
}
