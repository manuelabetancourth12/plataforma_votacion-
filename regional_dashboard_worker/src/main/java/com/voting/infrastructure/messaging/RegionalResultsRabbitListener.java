package com.voting.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voting.infrastructure.websocket.RegionalWebSocketHandler;
import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RegionalResultsRabbitListener {

    private final RegionalWebSocketHandler webSocketHandler;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RegionalResultsRabbitListener(RegionalWebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @Bean
    public TopicExchange liveResultsRegionalExchange() {
        return new TopicExchange("live_results_regional");
    }

    @Bean
    public Queue regionalDashboardQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding regionalDashboardBinding(Queue regionalDashboardQueue, TopicExchange liveResultsRegionalExchange) {
        return BindingBuilder.bind(regionalDashboardQueue).to(liveResultsRegionalExchange).with("results.*");
    }

    @RabbitListener(queues = "#{regionalDashboardQueue.name}")
    public void onRegionalResults(Map<String, Object> payload) {
        try {
            webSocketHandler.broadcast(objectMapper.writeValueAsString(payload));
        } catch (Exception e) {
            System.err.println("Error serializando resultados regionales: " + e.getMessage());
        }
    }
}
