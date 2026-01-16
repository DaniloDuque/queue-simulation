package org.sim.distribution;

import com.google.inject.Inject;
import org.apache.commons.math3.distribution.ExponentialDistribution;

public class ExponentialServiceTimeDistribution implements ServiceTimeDistribution {

    private final ExponentialDistribution dist;

    @Inject
    public ExponentialServiceTimeDistribution(final double mean) {
        dist = new ExponentialDistribution(mean);
    }

    @Override
    public double sample() {
        return dist.sample();
    }
}
