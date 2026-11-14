package com.voting.domain;

public interface RiskState {
    void registerActivity(VoterActivityContext context);
    String getLevel();
}