package com.voting.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Repositorio Spring Data JPA para la tabla security_incidents. */
@Repository
public interface JpaSecurityIncidentRepository extends JpaRepository<SecurityIncidentEntity, Long> {
}
