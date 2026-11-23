package com.voting.domain.port;

import com.voting.domain.model.IpAddress;

/** Puerto de salida para consultar y registrar direcciones IP bloqueadas. */
public interface BlockedIpStore {
    void blockIp(IpAddress ipAddress);
    boolean isBlocked(IpAddress ipAddress);
}
