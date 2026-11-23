package com.voting.domain;

/** Estrategia de dominio para calcular el peso de un voto según su región. */
public interface RegionWeightStrategy {
    double weightFor(String regionId);
}
