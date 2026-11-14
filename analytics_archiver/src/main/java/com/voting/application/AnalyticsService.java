package com.voting.application;

import com.voting.infrastructure.persistence.VoteEntity;
import com.voting.infrastructure.persistence.VoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
public class AnalyticsService {

    private final VoteRepository voteRepository;

    public AnalyticsService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @Transactional
    public void archiveVote(String voterId, String candidateId, String regionId, Instant timestamp) {
        Optional<VoteEntity> existingVote = voteRepository.findByVoterId(voterId);

        if (existingVote.isPresent()) {
            // Si el voto ya existe (el usuario cambió su voto), en un sistema de auditoría real
            // podríamos guardar un histórico de cambios, pero aquí mantenemos el estado final.
            System.out.println("Actualizando registro de auditoría para votante: " + voterId);
        } else {
            VoteEntity newVote = new VoteEntity(voterId, candidateId, regionId, timestamp);
            voteRepository.save(newVote);
            System.out.println("Voto archivado en PostgreSQL exitosamente para: " + voterId);
        }
    }
}