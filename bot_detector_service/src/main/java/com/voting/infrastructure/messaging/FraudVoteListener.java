package com.voting.infrastructure.messaging;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voting.application.FraudDetectionService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class FraudVoteListener {

    private final FraudDetectionService fraudService;
    private final ObjectMapper mapper = new ObjectMapper();

    public FraudVoteListener(FraudDetectionService fraudService) {
        this.fraudService = fraudService;
    }

    @KafkaListener(topics = "raw_votes", groupId = "bot_detector_group")
    public void consumeForAnalysis(String voteJson) {
        try {
            JsonNode root = mapper.readTree(voteJson);
            String voterId = root.path("voterId").path("value").asText();
            fraudService.analyzeVoteStream(voterId);
        } catch (Exception e) {
            System.err.println("Error analizando evento para fraude: " + e.getMessage());
        }
    }
}