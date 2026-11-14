package com.voting.domain;

import java.util.Map;

public interface LiveResultPublisher {
    void broadcast(long totalVotes, Map<String, Long> candidateVotes);
}