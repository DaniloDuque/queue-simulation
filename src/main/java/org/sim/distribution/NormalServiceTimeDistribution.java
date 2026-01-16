package org.sim.distribution;

import com.google.inject.Inject;
import org.apache.commons.math3.distribution.NormalDistribution;

public class NormalServiceTimeDistribution implements ServiceTimeDistribution {

    private final NormalDistribution dist;

    @Inject
    public NormalServiceTimeDistribution(final double mean, final double stdDev) {
        dist = new NormalDistribution(mean, stdDev);
    }

    // This is ceiled due to the project requirements
    @Override
    public double sample() {
        return Math.ceil(dist.sample());
    }
}
