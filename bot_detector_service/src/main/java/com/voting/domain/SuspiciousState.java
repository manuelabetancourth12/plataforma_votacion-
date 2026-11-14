package com.voting.domain;

public class SuspiciousState implements RiskState {
    @Override
    public void registerActivity(VoterActivityContext context) {
        if (context.getActivityCount() > 5) {
            context.setState(new BlockedState());
        }
    }
    @Override
    public String getLevel() { return "SOSPECHOSA"; }
}