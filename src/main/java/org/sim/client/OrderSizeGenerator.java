package org.sim.client;

import org.apache.commons.math3.distribution.BinomialDistribution;
import org.sim.module.Constants;

public class OrderSizeGenerator {
	private static final BinomialDistribution dist = new BinomialDistribution(
			Constants.CLIENT_NUMBER_OF_ORDER_PER_STATION_N, Constants.CLIENT_NUMBER_OF_ORDER_PER_STATION_P);

	public static int generate() {
		return Math.max(1, dist.sample());
	}
}
