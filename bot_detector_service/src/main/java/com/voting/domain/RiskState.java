package com.voting.domain;

public interface RiskState {
    void registerActivity(IpActivityContext context);
    String getLevel();
}
