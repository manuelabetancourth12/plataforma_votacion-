package com.voting.domain;

/** Puerto de salida para persistir incidentes de seguridad detectados. */
public interface SecurityIncidentRepository {
    void saveIncident(String ipAddress, String riskLevel, int activityCount);
}
