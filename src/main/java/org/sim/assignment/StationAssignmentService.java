package org.sim.assignment;

import lombok.AllArgsConstructor;
import org.sim.station.ServiceStation;
import org.sim.station.StationName;
import org.sim.station.StationSpecification;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

@AllArgsConstructor
public class StationAssignmentService implements Iterator<Map<StationName, ServiceStation>> {
	private final CompositionGenerator compositionGenerator;
	private final Map<StationName, StationSpecification> stationSpecifications;

	@Override
	public boolean hasNext() {
		return compositionGenerator.hasNext();
	}

	@Override
	public Map<StationName, ServiceStation> next() throws NoSuchElementException, IllegalStateException {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}

		final int[] composition = compositionGenerator.next();
		if (composition.length != stationSpecifications.size()) {
			throw new IllegalStateException("Composition length does not match station specifications");
		}

		final Map<StationName, ServiceStation> serviceStations = new HashMap<>();

		int i = 0;
		for (final StationName stationName : stationSpecifications.keySet()) {
			final StationSpecification stationSpecification = stationSpecifications.get(stationName);
			final ServiceStation serviceStation = new ServiceStation(stationSpecification, composition[i]);
			serviceStations.put(stationName, serviceStation);
			++i;
		}

		return serviceStations;
	}
}
