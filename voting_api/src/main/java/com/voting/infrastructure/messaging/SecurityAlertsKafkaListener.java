package com.voting.infrastructure.messaging;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voting.domain.model.IpAddress;
import com.voting.domain.port.BlockedIpStore;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/** Consumidor Kafka que escucha security_alerts para bloquear IPs marcadas como BLOQUEADA. */
@Component
public class SecurityAlertsKafkaListener {

    private final BlockedIpStore blockedIpStore;
    private final ObjectMapper mapper = new ObjectMapper();

    public SecurityAlertsKafkaListener(BlockedIpStore blockedIpStore) {
        this.blockedIpStore = blockedIpStore;
    }

    @KafkaListener(topics = "security_alerts", groupId = "voting_api_security_group", properties = {"auto.offset.reset=earliest"})
    public void consumeSecurityAlert(String alertJson) {
        try {
            JsonNode root = mapper.readTree(alertJson);
            String ipAddress = root.path("ipAddress").asText();
            String riskLevel = root.path("riskLevel").asText();

            if ("BLOQUEADA".equalsIgnoreCase(riskLevel) && ipAddress != null && !ipAddress.isBlank()) {
                blockedIpStore.blockIp(new IpAddress(ipAddress));
                System.out.println("🚫 IP bloqueada en voting_api: " + ipAddress);
            }
        } catch (Exception e) {
            System.err.println("Error procesando alerta de seguridad: " + e.getMessage());
        }
    }
}
