package com.voting.domain;

import java.util.Map;

/** Puerto de salida para difundir el conteo global de votos en tiempo real. */
public interface LiveResultPublisher {
    void broadcast(long totalVotes, Map<String, Long> candidateVotes);
}