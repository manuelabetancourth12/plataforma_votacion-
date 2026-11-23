package com.voting.domain;

/** Puerto de salida para publicar votantes elegibles hacia la plataforma. */
public interface EligibleVoterPublisher {
    void publishEligibleVoter(VoterRecord voter);
}
