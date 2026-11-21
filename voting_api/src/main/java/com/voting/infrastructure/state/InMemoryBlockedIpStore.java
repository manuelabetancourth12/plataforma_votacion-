package com.voting.infrastructure.state;

import com.voting.domain.model.IpAddress;
import com.voting.domain.port.BlockedIpStore;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryBlockedIpStore implements BlockedIpStore {
    private final Set<IpAddress> blockedIps = ConcurrentHashMap.newKeySet();

    @Override
    public void blockIp(IpAddress ipAddress) {
        blockedIps.add(ipAddress);
    }

    @Override
    public boolean isBlocked(IpAddress ipAddress) {
        return blockedIps.contains(ipAddress);
    }
}
