package com.voting.domain;

public class IpActivityContext {
    private final String ipAddress;
    private RiskState currentState;
    private int activityCount;

    public IpActivityContext(String ipAddress) {
        if (ipAddress == null || ipAddress.isBlank()) {
            throw new IllegalArgumentException("La dirección IP no puede ser nula ni vacía");
        }
        this.ipAddress = ipAddress;
        this.currentState = new NormalState();
        this.activityCount = 0;
    }

    public synchronized void registerVote() {
        this.activityCount++;
        this.currentState.registerActivity(this);
    }

    public synchronized void setState(RiskState newState) {
        this.currentState = newState;
    }

    public synchronized int getActivityCount() { return activityCount; }
    public String getIpAddress() { return ipAddress; }
    public synchronized String getCurrentRiskLevel() { return currentState.getLevel(); }
}
