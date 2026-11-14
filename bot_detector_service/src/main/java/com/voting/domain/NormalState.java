package com.voting.domain;

public class NormalState implements RiskState {
    @Override
    public void registerActivity(VoterActivityContext context) {
        if (context.getActivityCount() > 3) {
            context.setState(new SuspiciousState());
        }
    }
    @Override
    public String getLevel() { return "NORMAL"; }
}