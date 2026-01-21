package org.sim.assignment;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import org.sim.generator.CompositionGenerator;
import org.sim.station.ServiceStation;
import org.sim.station.StationName;
import org.sim.station.StationSpecification;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

@AllArgsConstructor
public class StationAssignmentService implements Iterator<StationAssignment> {
	private final CompositionGenerator compositionGenerator;
	private final ImmutableMap<StationName, StationSpecification> stationSpecifications;

	@Override
	public boolean hasNext() {
		return compositionGenerator.hasNext();
	}

	@Override
	public StationAssignment next() throws NoSuchElementException, IllegalStateException {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}

		final int[] composition = compositionGenerator.next();
		if (composition.length != stationSpecifications.size()) {
			throw new IllegalStateException("Composition length does not match station specifications");
		}

		final Map<StationName, ServiceStation> serviceStations = new HashMap<>();
		final Map<StationName, Integer> workerCounts = new HashMap<>();

		int i = 0;
		for (final StationName stationName : stationSpecifications.keySet()) {
			final int workers = composition[i];
			final StationSpecification stationSpecification = stationSpecifications.get(stationName);
			final ServiceStation serviceStation = new ServiceStation(stationSpecification, workers);
			serviceStations.put(stationName, serviceStation);
			workerCounts.put(stationName, workers);
			++i;
		}

		return new StationAssignment(ImmutableMap.copyOf(serviceStations), ImmutableMap.copyOf(workerCounts)); // Make
																												// the
																												// maps
																												// read-only
	}
}
