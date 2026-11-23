package com.voting.infrastructure.messaging;

import com.voting.domain.model.VoterId;
import com.voting.domain.port.EligibilityStore;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/** Consumidor Kafka que sincroniza el padrón de votantes desde el tópico eligible_voters. */
@Component
public class EligibilityKafkaListener implements ConsumerSeekAware {

    private final EligibilityStore eligibilityStore;

    public EligibilityKafkaListener(EligibilityStore eligibilityStore) {
        this.eligibilityStore = eligibilityStore;
    }

    @KafkaListener(topics = "eligible_voters", groupId = "voting_api_group", properties = {"auto.offset.reset=earliest"})
    public void consumeEligibleVoter(String voterIdStr) {
        if (voterIdStr != null && !voterIdStr.isBlank()) {
            eligibilityStore.addEligibleVoter(new VoterId(voterIdStr.trim()));
        }
    }

    @Override
    public void onPartitionsAssigned(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
        // Garantiza materializar toda la KTable desde el inicio del topic compactado al arrancar/rebalancear
        callback.seekToBeginning(assignments.keySet());
    }
}
