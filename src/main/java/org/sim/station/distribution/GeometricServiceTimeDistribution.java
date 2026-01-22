package org.sim.station.distribution;

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
		return dist.sample() * Constants.SECONDS_IN_MINUTE;
	}
}
