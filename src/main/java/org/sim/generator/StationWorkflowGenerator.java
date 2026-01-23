package org.sim.generator;

import com.google.common.collect.ImmutableMap;
import lombok.NonNull;
import org.sim.station.assignment.StationAssignment;
import org.sim.module.Constants;
import org.sim.station.Station;
import org.sim.station.StationName;
import org.sim.station.router.StationWorkflow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class StationWorkflowGenerator {
	private static final ThreadLocal<Random> threadLocalRandom = ThreadLocal.withInitial(Random::new);

	public static StationWorkflow generate(@NonNull final StationAssignment stationAssignment) {
		final ImmutableMap<StationName, Station> stations = stationAssignment.getStations();

		final Station cashier = stations.get(StationName.CASHIER);
		final Station drinks = stations.get(StationName.DRINKS);
		final Station frier = stations.get(StationName.FRYER);
		final Station chicken = stations.get(StationName.CHICKEN);

		if (cashier == null || drinks == null || frier == null || chicken == null) {
			throw new IllegalStateException("Missing station");
		}

		final List<StationWorkflow> nextStations = new ArrayList<>();

		if (threadLocalRandom.get().nextDouble() < Constants.DRINKS_PROB) {
			final StationWorkflow drinksStationWorkflow = new StationWorkflow(drinks, Collections.emptyList());
			nextStations.add(drinksStationWorkflow);
		}
		if (threadLocalRandom.get().nextDouble() < Constants.FRYER_PROB) {
			final StationWorkflow frierStationWorkflow = new StationWorkflow(frier, Collections.emptyList());
			nextStations.add(frierStationWorkflow);
		}
		if (threadLocalRandom.get().nextDouble() < Constants.CHICKEN_PROB) {
			final StationWorkflow chickenStationWorkflow = new StationWorkflow(chicken, Collections.emptyList());
			nextStations.add(chickenStationWorkflow);
		}

		return new StationWorkflow(cashier, nextStations);
	}
}
