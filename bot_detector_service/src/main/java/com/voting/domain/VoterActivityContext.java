package com.voting.domain;

public class VoterActivityContext {
    private final String voterId;
    private RiskState currentState;
    private int activityCount;

    public VoterActivityContext(String voterId) {
        this.voterId = voterId;
        this.currentState = new NormalState(); // Estado inicial
        this.activityCount = 0;
    }

    public void registerVote() {
        this.activityCount++;
        // Delegamos el comportamiento al estado actual (Polimorfismo)
        this.currentState.registerActivity(this);
    }

    public void setState(RiskState newState) {
        this.currentState = newState;
    }

    public int getActivityCount() { return activityCount; }
    public String getVoterId() { return voterId; }
    public String getCurrentRiskLevel() { return currentState.getLevel(); }
}