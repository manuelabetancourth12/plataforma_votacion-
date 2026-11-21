package com.voting.domain;

import java.util.Map;

public class CustomRegionWeightStrategy implements RegionWeightStrategy {
    private final Map<String, Double> weightsByRegion;

    public CustomRegionWeightStrategy(Map<String, Double> weightsByRegion) {
        this.weightsByRegion = Map.copyOf(weightsByRegion);
    }

    @Override
    public double weightFor(String regionId) {
        if (regionId == null) {
            return 1.0;
        }
        return weightsByRegion.getOrDefault(regionId.toUpperCase(), 1.0);
    }
}
