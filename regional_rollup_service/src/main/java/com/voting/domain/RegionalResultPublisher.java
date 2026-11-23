package com.voting.domain;

import java.util.Map;

/** Puerto de salida para publicar los resultados acumulados de una región. */
public interface RegionalResultPublisher {
    void publishRegionResults(String regionId, Map<String, Double> candidateScores);
}
