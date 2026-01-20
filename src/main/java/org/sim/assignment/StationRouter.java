package org.sim.assignment;

import java.util.*;

import lombok.NonNull;
import org.sim.module.Constants;
import org.sim.station.ServiceStation;
import org.sim.station.StationName;
import org.sim.station.StationSpecification;
import org.sim.station.StationWorkflow;

public class StationRouter implements Iterator<StationWorkflow> {
	private final StationAssignmentService stationAssignmentService;

	public StationRouter(final int workers,
			@NonNull final Map<StationName, StationSpecification> stationSpecifications) {
		final int CompositionSize = stationSpecifications.size();
		final CompositionGenerator compositionGenerator = new CompositionGenerator(workers, CompositionSize);
		stationAssignmentService = new StationAssignmentService(compositionGenerator, stationSpecifications);
	}

	@Override
	public boolean hasNext() {
		return stationAssignmentService.hasNext();
	}

	@Override
	public StationWorkflow next() throws NoSuchElementException {
		if (!stationAssignmentService.hasNext()) {
			throw new NoSuchElementException();
		}
		final Map<StationName, ServiceStation> stations = stationAssignmentService.next();

		final ServiceStation cashier = stations.get(StationName.CASHIER);
		final ServiceStation drinks = stations.get(StationName.DRINKS);
		final ServiceStation frier = stations.get(StationName.FRIER);
		final ServiceStation desert = stations.get(StationName.DESERT);
		final ServiceStation chicken = stations.get(StationName.CHICKEN);

		if (cashier == null || drinks == null || frier == null || desert == null || chicken == null) {
			throw new IllegalStateException("Missing station");
		}

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
