package com.voting.infrastructure.bootstrap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voting.application.VoterRegistrationService;
import com.voting.domain.VoterRecord;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

/** Ejecutor inicial que carga voters.json al arrancar la aplicación. */
@Component
public class VoterRegistrationRunner implements ApplicationRunner {

    private final VoterRegistrationService registrationService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public VoterRegistrationRunner(VoterRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ClassPathResource resource = new ClassPathResource("voters.json");
        try (InputStream is = resource.getInputStream()) {
            List<VoterRecord> voters = objectMapper.readValue(is, new TypeReference<>() {});
            int count = registrationService.registerEligibleVoters(voters);
            System.out.println("✅ Votantes elegibles publicados en Kafka (eligible_voters): " + count);
        }
    }
}
