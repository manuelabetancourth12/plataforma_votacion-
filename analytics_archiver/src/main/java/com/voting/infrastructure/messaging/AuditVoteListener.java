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
            
            Instant timestamp = parseTimestamp(root.path("timestamp"));

            analyticsService.archiveVote(voterId, candidateId, regionId, timestamp);

        } catch (Exception e) {
            System.err.println("Error procesando voto para auditoría: " + e.getMessage());
        }
    }

    private Instant parseTimestamp(JsonNode timestampNode) {
        if (timestampNode == null || timestampNode.isMissingNode() || timestampNode.isNull()) {
            return Instant.now();
        }
        if (timestampNode.isNumber()) {
            long seconds = timestampNode.longValue();
            long nanos = Math.round((timestampNode.doubleValue() - seconds) * 1_000_000_000L);
            return Instant.ofEpochSecond(seconds, nanos);
        }
        String text = timestampNode.asText();
        return (text == null || text.isBlank()) ? Instant.now() : Instant.parse(text);
    }
}