package com.voting.domain.port;

import com.voting.domain.model.VoterId;

public interface EligibilityStore {
    void addEligibleVoter(VoterId voterId);
    boolean isEligible(VoterId voterId);
}
