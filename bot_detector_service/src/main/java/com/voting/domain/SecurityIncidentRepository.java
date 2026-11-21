package com.voting.domain;

public interface SecurityIncidentRepository {
    void saveIncident(String ipAddress, String riskLevel, int activityCount);
}
