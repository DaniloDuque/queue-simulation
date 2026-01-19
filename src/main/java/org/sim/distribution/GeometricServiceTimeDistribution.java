package org.sim.distribution;

import com.google.inject.Inject;
import org.apache.commons.math3.distribution.GeometricDistribution;
import org.sim.module.Constants;

public class GeometricServiceTimeDistribution implements ServiceTimeDistribution {

	private final GeometricDistribution dist;

	@Inject
	public GeometricServiceTimeDistribution(final double p) {
		dist = new GeometricDistribution(p);
	}

	@Override
	public double sample() {
		// Geometric distribution for chicken: p=0.1, x is minutes
		// Convert to seconds: geometric result is already in minutes
		return dist.sample() * Constants.SECONDS_IN_MINUTE;
	}
}
