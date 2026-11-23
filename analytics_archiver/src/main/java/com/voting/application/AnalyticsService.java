package com.voting.application;

import com.voting.infrastructure.persistence.VoteEntity;
import com.voting.infrastructure.persistence.VoteMapper;
import com.voting.infrastructure.persistence.VoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

/** Servicio de aplicación que inserta o actualiza el voto auditable por votante. */
@Service
public class AnalyticsService {

    private final VoteRepository voteRepository;
    private final VoteMapper voteMapper;

    public AnalyticsService(VoteRepository voteRepository, VoteMapper voteMapper) {
        this.voteRepository = voteRepository;
        this.voteMapper = voteMapper;
    }

    @Transactional
    public void archiveVote(String voterId, String candidateId, String regionId, Instant timestamp) {
        Optional<VoteEntity> existingVote = voteRepository.findByVoterId(voterId);

        if (existingVote.isPresent()) {
            VoteEntity entity = existingVote.get();
            entity.updateVote(candidateId, regionId, timestamp);
            voteRepository.save(entity);
            System.out.println("Registro de auditoría actualizado para votante: " + voterId);
        } else {
            VoteEntity newVote = voteMapper.toEntity(voterId, candidateId, regionId, timestamp);
            voteRepository.save(newVote);
            System.out.println("Voto archivado en PostgreSQL exitosamente para: " + voterId);
        }
    }
}
