package com.voting.infrastructure.persistence;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "security_incidents")
public class SecurityIncidentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ip_address", nullable = false)
    private String ipAddress;

    @Column(name = "risk_level", nullable = false)
    private String riskLevel;

    @Column(name = "activity_count", nullable = false)
    private int activityCount;

    @Column(name = "detected_at", nullable = false)
    private Instant detectedAt;

    protected SecurityIncidentEntity() {}

    public SecurityIncidentEntity(String ipAddress, String riskLevel, int activityCount, Instant detectedAt) {
        this.ipAddress = ipAddress;
        this.riskLevel = riskLevel;
        this.activityCount = activityCount;
        this.detectedAt = detectedAt;
    }

    public Long getId() { return id; }
    public String getIpAddress() { return ipAddress; }
    public String getRiskLevel() { return riskLevel; }
    public int getActivityCount() { return activityCount; }
    public Instant getDetectedAt() { return detectedAt; }
}
