package com.voting.domain;

public interface SecurityAlertPublisher {
    void publishAlert(String voterId, String riskLevel);
}