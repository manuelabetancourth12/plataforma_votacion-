package com.voting.infrastructure.persistence;

import jakarta.persistence.*;
import java.time.Instant;

/** Entidad JPA mapeada a audit_votes; conserva el último voto final por votante. */
@Entity
@Table(name = "audit_votes")
public class VoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "voter_id", nullable = false, unique = true) // unique asegura que solo quede 1 voto final por usuario en auditoría
    private String voterId;

    @Column(name = "candidate_id", nullable = false)
    private String candidateId;

    @Column(name = "region_id", nullable = false)
    private String regionId;

    @Column(name = "vote_timestamp", nullable = false)
    private Instant timestamp;

    // JPA exige un constructor vacío
    protected VoteEntity() {}

    public VoteEntity(String voterId, String candidateId, String regionId, Instant timestamp) {
        this.voterId = voterId;
        this.candidateId = candidateId;
        this.regionId = regionId;
        this.timestamp = timestamp;
    }

    public void updateVote(String candidateId, String regionId, Instant timestamp) {
        if (candidateId == null || candidateId.isBlank() || regionId == null || regionId.isBlank() || timestamp == null) {
            throw new IllegalArgumentException("Datos de actualización de voto inválidos");
        }
        this.candidateId = candidateId;
        this.regionId = regionId;
        this.timestamp = timestamp;
    }

    // Getters
    public Long getId() { return id; }
    public String getVoterId() { return voterId; }
    public String getCandidateId() { return candidateId; }
    public String getRegionId() { return regionId; }
    public Instant getTimestamp() { return timestamp; }
}