package com.voting.domain;

import java.util.Map;

public interface RegionalResultPublisher {
    void publishRegionResults(String regionId, Map<String, Double> candidateScores);
}
