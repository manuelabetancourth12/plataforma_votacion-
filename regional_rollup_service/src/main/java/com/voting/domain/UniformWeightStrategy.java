package com.voting.domain;

import org.springframework.stereotype.Component;

/** Estrategia por defecto que asigna un peso uniforme de 1.0 a todas las regiones. */
@Component
public class UniformWeightStrategy implements RegionWeightStrategy {
    @Override
    public double weightFor(String regionId) {
        return 1.0;
    }
}
