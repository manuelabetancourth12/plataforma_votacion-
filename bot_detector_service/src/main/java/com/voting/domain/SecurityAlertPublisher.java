package com.voting.domain;

public interface SecurityAlertPublisher {
    void publishAlert(String ipAddress, String riskLevel);
}
