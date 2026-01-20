package org.sim.station;

import lombok.Getter;
import lombok.NonNull;

import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;

@Getter
public class StationWorkflow {
	private final ServiceStation currentStation;
	private final Collection<StationWorkflow> nextStations;

	public StationWorkflow(@NonNull final ServiceStation currentStation,
			@NonNull final Collection<StationWorkflow> nextStations) {
		this.currentStation = currentStation;
		this.nextStations = nextStations;
	}

	public StationWorkflow deepCopy() {
		ServiceStation copiedStation = new ServiceStation(currentStation.getDist(), currentStation.getWorkers(),
				new LinkedList<>());
		Collection<StationWorkflow> copiedNextStations = nextStations.stream()
				.map(StationWorkflow::deepCopy)
				.collect(Collectors.toList());
		return new StationWorkflow(copiedStation, copiedNextStations);
	}
}
