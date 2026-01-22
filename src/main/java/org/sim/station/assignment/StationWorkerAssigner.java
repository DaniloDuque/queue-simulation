package org.sim.station.assignment;

import com.google.common.collect.ImmutableMap;
import lombok.NonNull;
import org.sim.stat.single.SimulationStatistics;
import org.sim.station.Station;
import org.sim.station.StationFactory;
import org.sim.station.StationName;
import org.sim.station.queue.StationQueue;
import org.sim.station.queue.StationQueueFactory;

import java.util.HashMap;
import java.util.Map;

public class StationWorkerAssigner {
	public static StationAssignment assignWorkers(@NonNull final StationConfiguration stationConfiguration,
			@NonNull final SimulationStatistics simulationStatistics) {
		final ImmutableMap<StationName, Integer> workerCountPerStation = stationConfiguration.workerCountPerStation();
		final ImmutableMap<StationName, StationSpecification> stationsSpecifications = stationConfiguration
				.stationSpecifications();
		final Map<StationName, Station> stations = new HashMap<>();

		for (final StationName stationName : stationsSpecifications.keySet()) {
			final StationSpecification stationSpecification = stationsSpecifications.get(stationName);
			final Integer workerCount = workerCountPerStation.getOrDefault(stationName, 0);
			final StationQueue stationQueue = StationQueueFactory.create(workerCount);
			final Station station = StationFactory.create(stationSpecification, stationQueue, simulationStatistics);
			stations.put(stationName, station);
		}

		return new StationAssignment(simulationStatistics, ImmutableMap.copyOf(stations));
	}
}
