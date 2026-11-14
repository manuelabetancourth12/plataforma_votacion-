package com.voting.application;

import com.voting.domain.model.CandidateId;
import com.voting.domain.model.RegionId;
import com.voting.domain.model.Vote;
import com.voting.domain.model.VoterId;
import com.voting.domain.port.VotePublisher;
import com.voting.infrastructure.state.EligibilityStateStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class VotingServiceTest {

    private VotePublisher votePublisherMock;
    private EligibilityStateStore eligibilityStoreMock;
    private VotingService votingService;

    @BeforeEach
    void setUp() {
        votePublisherMock = mock(VotePublisher.class);
        eligibilityStoreMock = mock(EligibilityStateStore.class);
        votingService = new VotingService(votePublisherMock, eligibilityStoreMock);
    }

    @Test
    void givenEligibleVoter_whenProcessVote_thenPublishVote() {
        VoterId voterId = new VoterId("VOTER-123");
        Vote vote = Vote.cast(voterId, new CandidateId("CAND-A"), new RegionId("NORTE"));
        when(eligibilityStoreMock.isEligible(voterId)).thenReturn(true);

        votingService.processVote(vote);

        verify(votePublisherMock, times(1)).publish(vote);
    }

    @Test
    void givenIneligibleVoter_whenProcessVote_thenThrowSecurityException() {
        VoterId voterId = new VoterId("FRAUD-999");
        Vote vote = Vote.cast(voterId, new CandidateId("CAND-A"), new RegionId("NORTE"));
        when(eligibilityStoreMock.isEligible(voterId)).thenReturn(false);

        assertThrows(SecurityException.class, () -> votingService.processVote(vote));
        verify(votePublisherMock, never()).publish(any());
    }
}