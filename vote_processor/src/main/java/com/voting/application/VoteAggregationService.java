package com.voting.application;

import com.voting.domain.GlobalVoteCounter;
import com.voting.domain.LiveResultPublisher;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class VoteAggregationService {

    private final GlobalVoteCounter counter = new GlobalVoteCounter();
    private final LiveResultPublisher publisher;

    public VoteAggregationService(LiveResultPublisher publisher) {
        this.publisher = publisher;
    }

    public void processNewVote(String candidateId) {
        counter.registerVote(candidateId);
    }

    // Tarea programada: Difunde los resultados cada 1 segundo (1000 ms)
    @Scheduled(fixedRate = 1000)
    public void broadcastResults() {
        publisher.broadcast(counter.getTotalVotes(), counter.getSnapshot());
    }
}