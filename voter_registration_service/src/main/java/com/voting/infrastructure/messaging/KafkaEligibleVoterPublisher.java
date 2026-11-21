package com.voting.infrastructure.messaging;

import com.voting.domain.EligibleVoterPublisher;
import com.voting.domain.VoterRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaEligibleVoterPublisher implements EligibleVoterPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topicName;

    public KafkaEligibleVoterPublisher(
            KafkaTemplate<String, String> kafkaTemplate,
            @Value("${kafka.topic.eligible-voters:eligible_voters}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    @Override
    public void publishEligibleVoter(VoterRecord voter) {
        kafkaTemplate.send(topicName, voter.voterId(), voter.voterId());
    }
}
