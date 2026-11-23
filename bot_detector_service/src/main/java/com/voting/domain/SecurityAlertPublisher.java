package com.voting.domain;

/** Puerto de salida para emitir alertas cuando una IP cambia de nivel de riesgo. */
public interface SecurityAlertPublisher {
    void publishAlert(String ipAddress, String riskLevel);
}
