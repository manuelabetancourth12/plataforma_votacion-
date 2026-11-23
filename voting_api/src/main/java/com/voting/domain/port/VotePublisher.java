package com.voting.domain.port;

import com.voting.domain.model.Vote;

/** Puerto de salida para publicar votos validados hacia el bus de eventos. */
public interface VotePublisher {
    void publish(Vote vote);
}