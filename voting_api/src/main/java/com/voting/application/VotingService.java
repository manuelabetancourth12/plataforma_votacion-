package com.voting.application;

import com.voting.domain.model.Vote;
import com.voting.domain.port.VotePublisher;
import com.voting.infrastructure.state.EligibilityStateStore;
import org.springframework.stereotype.Service;

@Service
public class VotingService {

    private final VotePublisher votePublisher;
    private final EligibilityStateStore eligibilityStore;

    public VotingService(VotePublisher votePublisher, EligibilityStateStore eligibilityStore) {
        this.votePublisher = votePublisher;
        this.eligibilityStore = eligibilityStore;
    }

    public void processVote(Vote vote) {
        if (!eligibilityStore.isEligible(vote.voterId())) {
            throw new SecurityException("Votante no elegible o no registrado: " + vote.voterId().value());
        }
        votePublisher.publish(vote);
    }
}