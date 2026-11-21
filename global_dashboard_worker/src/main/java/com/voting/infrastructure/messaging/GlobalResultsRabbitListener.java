package com.voting.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voting.infrastructure.websocket.GlobalWebSocketHandler;
import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GlobalResultsRabbitListener {

    private final GlobalWebSocketHandler webSocketHandler;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GlobalResultsRabbitListener(GlobalWebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @Bean
    public FanoutExchange liveResultsGlobalExchange() {
        return new FanoutExchange("live_results_global");
    }

    @Bean
    public Queue globalDashboardQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding globalDashboardBinding(Queue globalDashboardQueue, FanoutExchange liveResultsGlobalExchange) {
        return BindingBuilder.bind(globalDashboardQueue).to(liveResultsGlobalExchange);
    }

    @RabbitListener(queues = "#{globalDashboardQueue.name}")
    public void onGlobalResults(Map<String, Object> payload) {
        try {
            webSocketHandler.broadcast(objectMapper.writeValueAsString(payload));
        } catch (Exception e) {
            System.err.println("Error serializando resultados globales: " + e.getMessage());
        }
    }
}
