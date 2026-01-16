package org.sim.distribution;

import com.google.inject.Inject;
import org.apache.commons.math3.distribution.BinomialDistribution;

public class BinomialServiceTimeDistribution implements ServiceTimeDistribution {

    private final BinomialDistribution dist;

    @Inject
    public BinomialServiceTimeDistribution(final int n, final double p) {
        dist = new BinomialDistribution(n, p);
    }
    @Override
    public double sample() {
        return dist.sample();
    }
}
