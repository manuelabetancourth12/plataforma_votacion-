package com.voting.infrastructure.messaging;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voting.application.AnalyticsService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class AuditVoteListener {

    private final AnalyticsService analyticsService;
    private final ObjectMapper mapper = new ObjectMapper();

    public AuditVoteListener(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @KafkaListener(topics = "raw_votes", groupId = "analytics_archiver_group")
    public void consumeVoteForAudit(String voteJson) {
        try {
            JsonNode root = mapper.readTree(voteJson);
            
            String voterId = root.path("voterId").path("value").asText();
            String candidateId = root.path("candidateId").path("value").asText();
            String regionId = root.path("regionId").path("value").asText();
            
            // Si la fecha viene en el JSON, la usamos, sino usamos la actual
            Instant timestamp = Instant.now(); 
            if (root.has("timestamp")) {
                timestamp = Instant.parse(root.path("timestamp").asText());
            }

            analyticsService.archiveVote(voterId, candidateId, regionId, timestamp);

        } catch (Exception e) {
            System.err.println("Error procesando voto para auditoría: " + e.getMessage());
        }
    }
}