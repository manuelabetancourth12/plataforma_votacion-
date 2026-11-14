package com.voting.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<VoteEntity, Long> {
    Optional<VoteEntity> findByVoterId(String voterId);
}