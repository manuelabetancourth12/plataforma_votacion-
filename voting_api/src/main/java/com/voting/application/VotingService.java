package com.voting.application;

import com.voting.domain.model.Vote;
import com.voting.domain.port.BlockedIpStore;
import com.voting.domain.port.EligibilityStore;
import com.voting.domain.port.VotePublisher;
import org.springframework.stereotype.Service;

@Service
public class VotingService {

    private final VotePublisher votePublisher;
    private final EligibilityStore eligibilityStore;
    private final BlockedIpStore blockedIpStore;

    public VotingService(
            VotePublisher votePublisher,
            EligibilityStore eligibilityStore,
            BlockedIpStore blockedIpStore) {
        this.votePublisher = votePublisher;
        this.eligibilityStore = eligibilityStore;
        this.blockedIpStore = blockedIpStore;
    }

    public void processVote(Vote vote) {
        if (blockedIpStore.isBlocked(vote.ipAddress())) {
            throw new SecurityException("IP bloqueada por actividad sospechosa: " + vote.ipAddress().value());
        }
        if (!eligibilityStore.isEligible(vote.voterId())) {
            throw new SecurityException("Votante no elegible o no registrado: " + vote.voterId().value());
        }
        votePublisher.publish(vote);
    }
}
