package com.voting.infrastructure.messaging;

import com.voting.domain.RegionalResultPublisher;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RabbitMQRegionalPublisher implements RegionalResultPublisher {

    public static final String EXCHANGE_NAME = "live_results_regional";
    private final RabbitTemplate rabbitTemplate;

    public RabbitMQRegionalPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Bean
    public TopicExchange regionalTopicExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Override
    public void publishRegionResults(String regionId, Map<String, Double> candidateScores) {
        if (candidateScores == null || candidateScores.isEmpty()) {
            return;
        }
        String routingKey = "results." + regionId.toLowerCase();
        Map<String, Object> payload = new HashMap<>();
        payload.put("region", regionId);
        payload.put("candidates", candidateScores);

        rabbitTemplate.convertAndSend(EXCHANGE_NAME, routingKey, payload);
        System.out.println("Resultados regionales publicados [" + routingKey + "]: " + candidateScores);
    }
}
