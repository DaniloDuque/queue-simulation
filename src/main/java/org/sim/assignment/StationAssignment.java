package org.sim.assignment;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.sim.station.ServiceStation;
import org.sim.station.StationName;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@Getter
@AllArgsConstructor
public class StationAssignment {
	private final ImmutableMap<StationName, ServiceStation> stations;
	private final ImmutableMap<StationName, Integer> workerCounts;

	public static StationAssignment copyOf(@NonNull final StationAssignment assignment) {
		final ImmutableMap<StationName, Integer> workerCounts = assignment.getWorkerCounts(); // no need to copy this
																								// map, since
		// it is read-only
		final Map<StationName, ServiceStation> newStations = new HashMap<>(); // we need to copy the stations, since
																				// they store and update state for each
																				// simulation

		for (final Map.Entry<StationName, ServiceStation> entry : assignment.getStations().entrySet()) {
			final StationName name = entry.getKey();
			final ServiceStation original = entry.getValue();
			final int workerCount = workerCounts.get(name);

			final ServiceStation newStation = new ServiceStation(original.getDist(), workerCount, new LinkedList<>());
			newStations.put(name, newStation);
		}

		return new StationAssignment(ImmutableMap.copyOf(newStations), workerCounts);
	}

}
