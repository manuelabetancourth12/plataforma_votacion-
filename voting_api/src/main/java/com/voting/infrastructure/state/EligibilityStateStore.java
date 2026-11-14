package com.voting.infrastructure.state;

import com.voting.domain.model.VoterId;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EligibilityStateStore {
    private final Set<VoterId> eligibleVoters = ConcurrentHashMap.newKeySet();

    @PostConstruct
    public void initTestVoters() {
        // Precargamos votantes simulados para probar la arquitectura
        eligibleVoters.add(new VoterId("VOTER-100"));
        eligibleVoters.add(new VoterId("VOTER-200"));
        eligibleVoters.add(new VoterId("VOTER-300"));
        System.out.println("✅ Votantes de prueba (VOTER-100, 200, 300) cargados en memoria.");
    }

    public void addEligibleVoter(VoterId voterId) {
        eligibleVoters.add(voterId);
    }

    public boolean isEligible(VoterId voterId) {
        return eligibleVoters.contains(voterId);
    }
}