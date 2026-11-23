package com.voting.application;

import com.voting.domain.RegionWeightStrategy;
import com.voting.domain.RegionalResultPublisher;
import com.voting.domain.RegionalVoteCounter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;

/** Servicio de aplicación que acumula votos por región y los publica cada 5 s. */
@Service
public class RegionalRollupService {

    private final RegionalVoteCounter counter;
    private final RegionalResultPublisher publisher;

    public RegionalRollupService(RegionWeightStrategy weightStrategy, RegionalResultPublisher publisher) {
        this.counter = new RegionalVoteCounter(weightStrategy);
        this.publisher = publisher;
    }

    public void processVote(String voterId, String regionId, String candidateId) {
        counter.registerVote(voterId, regionId, candidateId);
    }

    @Scheduled(fixedRate = 5000)
    public void broadcastRegionalResults() {
        Map<String, Map<String, Double>> snapshot = counter.getSnapshot();
        snapshot.forEach(publisher::publishRegionResults);
    }
}
