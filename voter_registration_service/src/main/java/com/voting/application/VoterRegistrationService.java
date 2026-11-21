package com.voting.application;

import com.voting.domain.EligibleVoterPublisher;
import com.voting.domain.VoterRecord;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoterRegistrationService {

    private final EligibleVoterPublisher publisher;

    public VoterRegistrationService(EligibleVoterPublisher publisher) {
        this.publisher = publisher;
    }

    public int registerEligibleVoters(List<VoterRecord> voters) {
        int publishedCount = 0;
        for (VoterRecord voter : voters) {
            if (voter != null && voter.eligible()) {
                publisher.publishEligibleVoter(voter);
                publishedCount++;
            }
        }
        return publishedCount;
    }
}
