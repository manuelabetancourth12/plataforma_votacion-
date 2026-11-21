package com.voting.infrastructure.messaging;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voting.application.VoteAggregationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class RawVoteKafkaListener {

    private final VoteAggregationService aggregationService;
    private final ObjectMapper mapper = new ObjectMapper();

    public RawVoteKafkaListener(VoteAggregationService aggregationService) {
        this.aggregationService = aggregationService;
    }

    @KafkaListener(topics = "raw_votes", groupId = "global_counter_group")
    public void consumeVote(String voteJson) {
        try {
            // Extraemos voterId y candidateId del JSON proveniente de Kafka
            JsonNode root = mapper.readTree(voteJson);
            String voterId = root.path("voterId").path("value").asText();
            String candidateId = root.path("candidateId").path("value").asText();

            aggregationService.processNewVote(voterId, candidateId);
        } catch (Exception e) {
            System.err.println("Error procesando voto: " + e.getMessage());
        }
    }
}