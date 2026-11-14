package com.voting.infrastructure.messaging;

import com.voting.domain.model.VoterId;
import com.voting.infrastructure.state.EligibilityStateStore;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EligibilityKafkaListener {

    private final EligibilityStateStore eligibilityStore;

    public EligibilityKafkaListener(EligibilityStateStore eligibilityStore) {
        this.eligibilityStore = eligibilityStore;
    }

    @KafkaListener(topics = "eligible_voters", groupId = "voting_api_group", properties = {"auto.offset.reset=earliest"})
    public void consumeEligibleVoter(String voterIdStr) {
        if (voterIdStr != null && !voterIdStr.isBlank()) {
            eligibilityStore.addEligibleVoter(new VoterId(voterIdStr));
        }
    }
}