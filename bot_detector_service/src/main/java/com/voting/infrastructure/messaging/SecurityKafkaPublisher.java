package com.voting.infrastructure.messaging;

import com.voting.domain.SecurityAlertPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/** Adaptador Kafka que publica alertas de seguridad en el tópico security_alerts. */
@Component
public class SecurityKafkaPublisher implements SecurityAlertPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public SecurityKafkaPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishAlert(String ipAddress, String riskLevel) {
        String alertMessage = String.format("{\"ipAddress\": \"%s\", \"riskLevel\": \"%s\"}", ipAddress, riskLevel);
        kafkaTemplate.send("security_alerts", ipAddress, alertMessage);
        System.out.println("⚠️ ALERTA DE SEGURIDAD EMITIDA: " + alertMessage);
    }
}
