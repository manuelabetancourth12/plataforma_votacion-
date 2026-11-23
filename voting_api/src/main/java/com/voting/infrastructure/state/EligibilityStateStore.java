package com.voting.infrastructure.state;

import com.voting.domain.model.VoterId;
import com.voting.domain.port.EligibilityStore;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/** Almacén en memoria concurrente para consultar votantes elegibles. */
@Component
public class EligibilityStateStore implements EligibilityStore {
    private final Set<VoterId> eligibleVoters = ConcurrentHashMap.newKeySet();

    @Value("${voting.eligibility.preload-test-voters:true}")
    private boolean preloadTestVoters;

    @PostConstruct
    public void initTestVoters() {
        if (!preloadTestVoters) {
            return;
        }
        // Precargamos votantes simulados cuando no hay voter_registration_service activo
        eligibleVoters.add(new VoterId("VOTER-100"));
        eligibleVoters.add(new VoterId("VOTER-200"));
        eligibleVoters.add(new VoterId("VOTER-300"));
        System.out.println("✅ Votantes de prueba (VOTER-100, 200, 300) cargados en memoria.");
    }

    @Override
    public void addEligibleVoter(VoterId voterId) {
        eligibleVoters.add(voterId);
    }

    @Override
    public boolean isEligible(VoterId voterId) {
        return eligibleVoters.contains(voterId);
    }
}