package com.voting.infrastructure.messaging;

import com.voting.domain.SecurityAlertPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class SecurityKafkaPublisher implements SecurityAlertPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public SecurityKafkaPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishAlert(String voterId, String riskLevel) {
        String alertMessage = String.format("{\"voterId\": \"%s\", \"riskLevel\": \"%s\"}", voterId, riskLevel);
        kafkaTemplate.send("security_alerts", voterId, alertMessage);
        System.out.println("⚠️ ALERTA DE SEGURIDAD EMITIDA: " + alertMessage);
    }
}