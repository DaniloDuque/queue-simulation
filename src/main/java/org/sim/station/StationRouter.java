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

	public StationWorkflow getStationWorkflow() {
		final List<StationWorkflow> nextStations = new ArrayList<>();

		if (Math.random() < Constants.DRINKS_PROB) {
			final StationWorkflow drinksStationWorkflow = new StationWorkflow(drinks, Collections.emptyList());
			nextStations.add(drinksStationWorkflow);
		}
		if (Math.random() < Constants.FRIER_PROB) {
			final StationWorkflow frierStationWorkflow = new StationWorkflow(frier, Collections.emptyList());
			nextStations.add(frierStationWorkflow);
		}
		if (Math.random() < Constants.DESERT_PROB) {
			final StationWorkflow desertStationWorkflow = new StationWorkflow(desert, Collections.emptyList());
			nextStations.add(desertStationWorkflow);
		}
		if (Math.random() < Constants.CHICKEN_PROB) {
			final StationWorkflow chickenStationWorkflow = new StationWorkflow(chicken, Collections.emptyList());
			nextStations.add(chickenStationWorkflow);
		}

		return new StationWorkflow(cashier, nextStations);
	}
}
