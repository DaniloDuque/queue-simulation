package org.sim.stat;

import lombok.extern.slf4j.Slf4j;
import org.sim.module.Constants;
import org.sim.station.StationName;

import java.util.EnumMap;
import java.util.Map;

@Slf4j
public class UtilizationCalculator {
	private static final double STABILITY_THRESHOLD = 0.8;

	public static Map<StationName, Double> calculateUtilizationRates(Map<StationName, Integer> workerConfiguration) {
		Map<StationName, Double> utilizationRates = new EnumMap<>(StationName.class);

		for (StationName station : StationName.values()) {
			double lambda = getEffectiveArrivalRate(station);
			double mu = getServiceRate(station);
			int servers = workerConfiguration.get(station);

			double utilization = lambda / (servers * mu);
			utilizationRates.put(station, utilization);
		}

		return utilizationRates;
	}

	public static boolean isSystemStable(Map<StationName, Integer> workerConfiguration) {
		return calculateUtilizationRates(workerConfiguration).values().stream()
				.allMatch(rho -> rho < STABILITY_THRESHOLD);
	}

	private static double getEffectiveArrivalRate(StationName station) {
		double baseRate = Constants.CLIENT_ARRIVAL_RATE_PER_SECOND;

		return switch (station) {
			case CASHIER -> baseRate * Constants.CASHIER_PROB;
			case DRINKS -> baseRate * Constants.DRINKS_PROB;
			case FRIER -> baseRate * Constants.FRIER_PROB;
			case DESERT -> baseRate * Constants.DESERT_PROB;
			case CHICKEN -> baseRate * Constants.CHICKEN_PROB;
		};
	}

	private static double getServiceRate(StationName station) {
		return switch (station) {
			case CASHIER -> 1.0 / (Constants.CASHIER_STATION_MEAN * Constants.SECONDS_IN_MINUTE);
			case DRINKS -> 1.0 / (Constants.DRINKS_STATION_MEAN * Constants.SECONDS_IN_MINUTE);
			case FRIER -> 1.0 / (Constants.FRIER_STATION_MEAN * Constants.SECONDS_IN_MINUTE);
			case DESERT ->
				1.0 / ((Constants.DESERT_STATION_N * Constants.DESERT_STATION_P) * Constants.SECONDS_IN_MINUTE);
			case CHICKEN -> 1.0 / ((1.0 / Constants.CHICKEN_STATION_P) * Constants.SECONDS_IN_MINUTE);
		};
	}
}
