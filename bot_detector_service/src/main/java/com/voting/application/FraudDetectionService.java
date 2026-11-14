package com.voting.application;

import com.voting.domain.SecurityAlertPublisher;
import com.voting.domain.VoterActivityContext;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FraudDetectionService {

    private final Map<String, VoterActivityContext> tracker = new ConcurrentHashMap<>();
    private final SecurityAlertPublisher alertPublisher;

    public FraudDetectionService(SecurityAlertPublisher alertPublisher) {
        this.alertPublisher = alertPublisher;
    }

    public void analyzeVoteStream(String voterId) {
        VoterActivityContext context = tracker.computeIfAbsent(voterId, VoterActivityContext::new);
        
        String previousState = context.getCurrentRiskLevel();
        context.registerVote(); // Aplica las reglas del patrón State internamente
        String newState = context.getCurrentRiskLevel();

        // Si escaló de nivel de riesgo, disparamos una alerta
        if (!previousState.equals(newState) && !newState.equals("NORMAL")) {
            alertPublisher.publishAlert(voterId, newState);
        }
    }
}