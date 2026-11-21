package com.voting.application;

import com.voting.domain.IpActivityContext;
import com.voting.domain.SecurityAlertPublisher;
import com.voting.domain.SecurityIncidentRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FraudDetectionService {

    private final Map<String, IpActivityContext> tracker = new ConcurrentHashMap<>();
    private final SecurityAlertPublisher alertPublisher;
    private final SecurityIncidentRepository incidentRepository;

    public FraudDetectionService(
            SecurityAlertPublisher alertPublisher,
            SecurityIncidentRepository incidentRepository) {
        this.alertPublisher = alertPublisher;
        this.incidentRepository = incidentRepository;
    }

    public void analyzeVoteStream(String ipAddress) {
        if (ipAddress == null || ipAddress.isBlank()) {
            return;
        }
        IpActivityContext context = tracker.computeIfAbsent(ipAddress, IpActivityContext::new);

        String previousState;
        String newState;
        int currentCount;
        synchronized (context) {
            previousState = context.getCurrentRiskLevel();
            context.registerVote();
            newState = context.getCurrentRiskLevel();
            currentCount = context.getActivityCount();
        }

        // Si escaló de nivel de riesgo, disparamos alerta en Kafka y registramos incidente en PostgreSQL
        if (!previousState.equals(newState) && !newState.equals("NORMAL")) {
            alertPublisher.publishAlert(ipAddress, newState);
            incidentRepository.saveIncident(ipAddress, newState, currentCount);
        }
    }
}
