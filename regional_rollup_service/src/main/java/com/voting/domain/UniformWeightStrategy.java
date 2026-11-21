package com.voting.domain;

import org.springframework.stereotype.Component;

@Component
public class UniformWeightStrategy implements RegionWeightStrategy {
    @Override
    public double weightFor(String regionId) {
        return 1.0;
    }
}
