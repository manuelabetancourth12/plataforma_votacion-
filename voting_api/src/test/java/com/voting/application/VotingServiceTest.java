package com.voting.application;

import com.voting.domain.model.CandidateId;
import com.voting.domain.model.IpAddress;
import com.voting.domain.model.RegionId;
import com.voting.domain.model.Vote;
import com.voting.domain.model.VoterId;
import com.voting.domain.port.BlockedIpStore;
import com.voting.domain.port.EligibilityStore;
import com.voting.domain.port.VotePublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class VotingServiceTest {

    private VotePublisher votePublisherMock;
    private EligibilityStore eligibilityStoreMock;
    private BlockedIpStore blockedIpStoreMock;
    private VotingService votingService;

    @BeforeEach
    void setUp() {
        votePublisherMock = mock(VotePublisher.class);
        eligibilityStoreMock = mock(EligibilityStore.class);
        blockedIpStoreMock = mock(BlockedIpStore.class);
        votingService = new VotingService(votePublisherMock, eligibilityStoreMock, blockedIpStoreMock);
    }

    @Test
    void givenEligibleVoterAndUnblockedIp_whenProcessVote_thenPublishVote() {
        // Arrange
        VoterId voterId = new VoterId("VOTER-123");
        IpAddress ip = new IpAddress("192.168.1.10");
        Vote vote = Vote.cast(voterId, new CandidateId("CAND-A"), new RegionId("NORTE"), ip);
        when(blockedIpStoreMock.isBlocked(ip)).thenReturn(false);
        when(eligibilityStoreMock.isEligible(voterId)).thenReturn(true);

        // Act
        votingService.processVote(vote);

        // Assert
        verify(votePublisherMock, times(1)).publish(vote);
    }

    @Test
    void givenIneligibleVoter_whenProcessVote_thenThrowSecurityException() {
        // Arrange
        VoterId voterId = new VoterId("FRAUD-999");
        IpAddress ip = new IpAddress("192.168.1.10");
        Vote vote = Vote.cast(voterId, new CandidateId("CAND-A"), new RegionId("NORTE"), ip);
        when(blockedIpStoreMock.isBlocked(ip)).thenReturn(false);
        when(eligibilityStoreMock.isEligible(voterId)).thenReturn(false);

        // Act & Assert
        assertThrows(SecurityException.class, () -> votingService.processVote(vote));
        verify(votePublisherMock, never()).publish(any());
    }

    @Test
    void givenBlockedIp_whenProcessVote_thenThrowSecurityException() {
        // Arrange
        VoterId voterId = new VoterId("VOTER-100");
        IpAddress ip = new IpAddress("203.0.113.50");
        Vote vote = Vote.cast(voterId, new CandidateId("CAND-A"), new RegionId("NORTE"), ip);
        when(blockedIpStoreMock.isBlocked(ip)).thenReturn(true);

        // Act & Assert
        assertThrows(SecurityException.class, () -> votingService.processVote(vote));
        verify(eligibilityStoreMock, never()).isEligible(any());
        verify(votePublisherMock, never()).publish(any());
    }
}
