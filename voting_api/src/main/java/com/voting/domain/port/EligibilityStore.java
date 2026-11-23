package com.voting.domain.port;

import com.voting.domain.model.VoterId;

/** Puerto de salida para gestionar el padrón de votantes elegibles. */
public interface EligibilityStore {
    void addEligibleVoter(VoterId voterId);
    boolean isEligible(VoterId voterId);
}
