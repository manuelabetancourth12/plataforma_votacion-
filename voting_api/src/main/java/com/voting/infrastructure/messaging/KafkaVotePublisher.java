package com.voting.infrastructure.messaging;

import com.voting.domain.model.Vote;
import com.voting.domain.port.VotePublisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/** Adaptador Kafka que publica votos validados en el tópico raw_votes. */
@Component
public class KafkaVotePublisher implements VotePublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String topicName;

    public KafkaVotePublisher(
            KafkaTemplate<String, Object> kafkaTemplate,
            @Value("${kafka.topic.raw-votes:raw_votes}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    @Override
    public void publish(Vote vote) {
        // La clave es el voterId para garantizar la de-duplicación en el tópico compactado
        kafkaTemplate.send(topicName, vote.voterId().value(), vote);
    }
}