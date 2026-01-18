package org.sim.station;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import lombok.AllArgsConstructor;

import java.util.*;

import org.sim.module.Constants;

@AllArgsConstructor(onConstructor_ = @Inject)
public class StationRouter {

	private final @Named(Constants.CASHIER_STATION_NAME) ServiceStation cashier;
	private final @Named(Constants.DRINKS_STATION_NAME) ServiceStation drinks;
	private final @Named(Constants.FRIER_STATION_NAME) ServiceStation frier;
	private final @Named(Constants.DESERT_STATION_NAME) ServiceStation desert;
	private final @Named(Constants.CHICKEN_STATION_NAME) ServiceStation chicken;

	public Queue<ServiceStation> getStationSequence() {
		final Queue<ServiceStation> stationSequence = new LinkedList<>();
		final List<ServiceStation> optionalStations = new ArrayList<>();

		// Cashier is always first
		if (Math.random() < Constants.CASHIER_PROB) {
			stationSequence.add(cashier);
		}

		if (Math.random() < Constants.DRINKS_PROB) {
			optionalStations.add(drinks);
		}
		if (Math.random() < Constants.FRIER_PROB) {
			optionalStations.add(frier);
		}
		if (Math.random() < Constants.DESERT_PROB) {
			optionalStations.add(desert);
		}
		if (Math.random() < Constants.CHICKEN_PROB) {
			optionalStations.add(chicken);
		}

		stationSequence.addAll(optionalStations);
		return stationSequence;
	}
}
