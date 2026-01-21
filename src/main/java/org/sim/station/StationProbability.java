package org.sim.station;

import com.google.common.collect.ImmutableMap;
import lombok.NonNull;
import org.sim.module.Constants;

import java.util.NoSuchElementException;

public class StationProbability {
	private static final ImmutableMap<StationName, Double> probabilities = ImmutableMap.of(
			StationName.CASHIER, Constants.CASHIER_PROB,
			StationName.DRINKS, Constants.DRINKS_PROB,
			StationName.FRIER, Constants.FRIER_PROB,
			StationName.DESERT, Constants.DESERT_PROB,
			StationName.CHICKEN, Constants.CHICKEN_PROB);

	public static Double of(@NonNull final StationName station) {
		final Double probability = probabilities.get(station);
		if (probability == null) {
			throw new NoSuchElementException();
		}
		return probability;
	}
}
