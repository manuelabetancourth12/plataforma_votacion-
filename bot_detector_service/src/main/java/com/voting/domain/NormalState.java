package com.voting.domain;

/** Estado NORMAL: escala a SOSPECHOSA si la IP supera 3 votos registrados. */
public class NormalState implements RiskState {
    @Override
    public void registerActivity(IpActivityContext context) {
        if (context.getActivityCount() > 3) {
            context.setState(new SuspiciousState());
        }
    }
    @Override
    public String getLevel() { return "NORMAL"; }
}