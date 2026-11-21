package com.voting.domain;

public class BlockedState implements RiskState {
    @Override
    public void registerActivity(IpActivityContext context) {
        // Una vez bloqueado, permanece bloqueado. 
        // No hay transición de salida en este escenario.
    }
    @Override
    public String getLevel() { return "BLOQUEADA"; }
}