package com.voting.infrastructure.messaging;

import com.voting.domain.LiveResultPublisher;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/** Adaptador RabbitMQ que publica el conteo global en el Fanout Exchange live_results_global. */
@Component
public class RabbitMQPublisher implements LiveResultPublisher {

    private final RabbitTemplate rabbitTemplate;
    private static final String EXCHANGE_NAME = "live_results_global";

    public RabbitMQPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    // Configura el Exchange tipo Fanout (transmisión a todos los dashboards conectados)
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(EXCHANGE_NAME);
    }

    @Override
    public void broadcast(long totalVotes, Map<String, Long> candidateVotes) {
        if (totalVotes == 0) return; // No saturar la red si no hay votos aún

        Map<String, Object> payload = new HashMap<>();
        payload.put("total", totalVotes);
        payload.put("candidates", candidateVotes);

        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "", payload);
        System.out.println("Resultados globales transmitidos: " + totalVotes + " votos.");
    }
}