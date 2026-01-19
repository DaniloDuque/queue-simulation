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
		// Binomial distribution for desserts: n=5, p=0.6
		// This should return the actual service time, not multiplied
		return dist.sample();
	}
}
