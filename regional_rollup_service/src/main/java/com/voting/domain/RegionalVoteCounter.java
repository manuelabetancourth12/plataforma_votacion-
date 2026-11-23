package com.voting.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** Contador de dominio por región y candidato con unicidad por votante y ponderación. */
public class RegionalVoteCounter {
    private record VoterSelection(String regionId, String candidateId, double weight) {}

    private final RegionWeightStrategy weightStrategy;
    private final Map<String, Map<String, Double>> scoresByRegionAndCandidate = new ConcurrentHashMap<>();
    private final Map<String, VoterSelection> lastVoteByVoter = new ConcurrentHashMap<>();

    public RegionalVoteCounter(RegionWeightStrategy weightStrategy) {
        this.weightStrategy = weightStrategy;
    }

    public synchronized void registerVote(String voterId, String regionId, String candidateId) {
        if (isBlank(voterId) || isBlank(regionId) || isBlank(candidateId)) {
            return;
        }
        String normalizedRegion = regionId.trim().toLowerCase();
        double weight = weightStrategy.weightFor(normalizedRegion);

        VoterSelection previous = lastVoteByVoter.put(
                voterId,
                new VoterSelection(normalizedRegion, candidateId, weight)
        );
        if (previous != null) {
            adjustScore(previous.regionId(), previous.candidateId(), -previous.weight());
        }
        adjustScore(normalizedRegion, candidateId, weight);
    }

    private void adjustScore(String region, String candidate, double delta) {
        Map<String, Double> candidates = scoresByRegionAndCandidate.computeIfAbsent(region, r -> new HashMap<>());
        double updated = Math.max(0.0, candidates.getOrDefault(candidate, 0.0) + delta);
        candidates.put(candidate, updated);
    }

    public synchronized Map<String, Map<String, Double>> getSnapshot() {
        Map<String, Map<String, Double>> snapshot = new HashMap<>();
        scoresByRegionAndCandidate.forEach((region, candidates) ->
                snapshot.put(region, Collections.unmodifiableMap(new HashMap<>(candidates))));
        return Collections.unmodifiableMap(snapshot);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
