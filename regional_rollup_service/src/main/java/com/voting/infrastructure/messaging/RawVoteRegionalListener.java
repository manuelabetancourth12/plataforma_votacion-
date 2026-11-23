package com.voting.infrastructure.messaging;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voting.application.RegionalRollupService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/** Consumidor Kafka que lee votos de raw_votes para la agregación regional. */
@Component
public class RawVoteRegionalListener {

    private final RegionalRollupService rollupService;
    private final ObjectMapper mapper = new ObjectMapper();

    public RawVoteRegionalListener(RegionalRollupService rollupService) {
        this.rollupService = rollupService;
    }

    @KafkaListener(topics = "raw_votes", groupId = "regional_rollup_group")
    public void consumeVote(String voteJson) {
        try {
            JsonNode root = mapper.readTree(voteJson);
            String voterId = root.path("voterId").path("value").asText();
            String regionId = root.path("regionId").path("value").asText();
            String candidateId = root.path("candidateId").path("value").asText();

            rollupService.processVote(voterId, regionId, candidateId);
        } catch (Exception e) {
            System.err.println("Error procesando voto regional: " + e.getMessage());
        }
    }
}
