package com.voting.domain;

public interface RegionWeightStrategy {
    double weightFor(String regionId);
}
