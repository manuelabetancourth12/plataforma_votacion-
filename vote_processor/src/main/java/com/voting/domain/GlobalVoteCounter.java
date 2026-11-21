package com.voting.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class GlobalVoteCounter {
    private final AtomicLong totalVotes = new AtomicLong(0);
    private final Map<String, AtomicLong> votesByCandidate = new ConcurrentHashMap<>();
    private final Map<String, String> lastVoteByVoter = new ConcurrentHashMap<>();

    // Garantiza unicidad del voto por votante (prevalece su último voto)
    public synchronized void registerVote(String voterId, String candidateId) {
        if (voterId == null || voterId.isBlank() || candidateId == null || candidateId.isBlank()) {
            return;
        }

        String previousCandidate = lastVoteByVoter.put(voterId, candidateId);
        if (previousCandidate == null) {
            totalVotes.incrementAndGet();
            votesByCandidate.computeIfAbsent(candidateId, k -> new AtomicLong(0)).incrementAndGet();
        } else if (!previousCandidate.equals(candidateId)) {
            AtomicLong prevCounter = votesByCandidate.get(previousCandidate);
            if (prevCounter != null && prevCounter.get() > 0) {
                prevCounter.decrementAndGet();
            }
            votesByCandidate.computeIfAbsent(candidateId, k -> new AtomicLong(0)).incrementAndGet();
        }
    }

    public long getTotalVotes() {
        return totalVotes.get();
    }

    public Map<String, Long> getSnapshot() {
        Map<String, Long> snapshot = new HashMap<>();
        votesByCandidate.forEach((key, value) -> snapshot.put(key, value.get()));
        return Collections.unmodifiableMap(snapshot);
    }
}
