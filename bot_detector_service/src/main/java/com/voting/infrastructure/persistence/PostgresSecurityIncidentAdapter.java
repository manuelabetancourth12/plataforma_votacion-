package com.voting.infrastructure.persistence;

import com.voting.domain.SecurityIncidentRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
public class PostgresSecurityIncidentAdapter implements SecurityIncidentRepository {

    private final JpaSecurityIncidentRepository jpaRepository;

    public PostgresSecurityIncidentAdapter(JpaSecurityIncidentRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    @Transactional
    public void saveIncident(String ipAddress, String riskLevel, int activityCount) {
        SecurityIncidentEntity entity = new SecurityIncidentEntity(ipAddress, riskLevel, activityCount, Instant.now());
        jpaRepository.save(entity);
    }
}
