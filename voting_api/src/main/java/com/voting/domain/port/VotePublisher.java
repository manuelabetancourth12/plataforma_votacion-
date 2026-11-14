package com.voting.domain.port;

import com.voting.domain.model.Vote;

public interface VotePublisher {
    void publish(Vote vote);
}