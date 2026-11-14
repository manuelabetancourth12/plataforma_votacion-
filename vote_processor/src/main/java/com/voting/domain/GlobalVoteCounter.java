package com.voting.domain;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class GlobalVoteCounter {
    private final AtomicLong totalVotes = new AtomicLong(0);
    private final Map<String, AtomicLong> votesByCandidate = new ConcurrentHashMap<>();

    // Comportamiento de negocio encapsulado
    public void registerVote(String candidateId) {
        if (candidateId == null || candidateId.isBlank()) return;
        
        totalVotes.incrementAndGet();
        votesByCandidate.computeIfAbsent(candidateId, k -> new AtomicLong(0)).incrementAndGet();
    }

    // Retorna instantáneas inmutables para evitar modificaciones externas
    public long getTotalVotes() {
        return totalVotes.get();
    }

    public Map<String, Long> getSnapshot() {
        Map<String, Long> snapshot = new ConcurrentHashMap<>();
        votesByCandidate.forEach((key, value) -> snapshot.put(key, value.get()));
        return Collections.unmodifiableMap(snapshot);
    }
}