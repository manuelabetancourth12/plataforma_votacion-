package com.voting.domain;

/** Contrato del patrón State para evaluar actividad y transiciones de riesgo por IP. */
public interface RiskState {
    void registerActivity(IpActivityContext context);
    String getLevel();
}
