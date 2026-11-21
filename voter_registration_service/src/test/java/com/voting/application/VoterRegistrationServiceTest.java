package com.voting.application;

import com.voting.domain.EligibleVoterPublisher;
import com.voting.domain.VoterRecord;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class VoterRegistrationServiceTest {

    @Test
    void givenMixedVoters_whenRegisterEligibleVoters_thenPublishOnlyEligibleOnes() {
        // Arrange
        EligibleVoterPublisher publisherMock = mock(EligibleVoterPublisher.class);
        VoterRegistrationService service = new VoterRegistrationService(publisherMock);
        VoterRecord eligible1 = new VoterRecord("VOTER-100", true);
        VoterRecord ineligible = new VoterRecord("VOTER-999", false);
        VoterRecord eligible2 = new VoterRecord("VOTER-200", true);

        // Act
        int count = service.registerEligibleVoters(List.of(eligible1, ineligible, eligible2));

        // Assert
        assertEquals(2, count);
        verify(publisherMock, times(1)).publishEligibleVoter(eligible1);
        verify(publisherMock, times(1)).publishEligibleVoter(eligible2);
        verify(publisherMock, never()).publishEligibleVoter(ineligible);
    }
}
