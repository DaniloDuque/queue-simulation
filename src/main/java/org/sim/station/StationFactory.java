package org.sim.station;

import lombok.NonNull;
import org.sim.stat.single.SimulationStatistics;
import org.sim.station.assignment.StationSpecification;
import org.sim.station.queue.StationQueue;

public class StationFactory {
	public static Station create(@NonNull final StationSpecification stationSpecification,
			@NonNull final StationQueue stationQueue, @NonNull final SimulationStatistics simulationStatistics) {
		return new Station(stationSpecification, stationQueue, simulationStatistics);
	}
}
