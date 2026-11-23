package com.voting.domain;

/** Estado SOSPECHOSA: escala a BLOQUEADA si la IP supera 5 votos registrados. */
public class SuspiciousState implements RiskState {
    @Override
    public void registerActivity(IpActivityContext context) {
        if (context.getActivityCount() > 5) {
            context.setState(new BlockedState());
        }
    }
    @Override
    public String getLevel() { return "SOSPECHOSA"; }
}