package com.voting.domain.port;

import com.voting.domain.model.IpAddress;

public interface BlockedIpStore {
    void blockIp(IpAddress ipAddress);
    boolean isBlocked(IpAddress ipAddress);
}
