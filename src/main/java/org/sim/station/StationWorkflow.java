package org.sim.station;

import lombok.NonNull;

import java.util.Collection;

public class StationWorkflow {
	private final ServiceStation currentStation;
	private final Collection<ServiceStation> nextStations;

	public StationWorkflow(@NonNull final ServiceStation currentStation,
			@NonNull final Collection<ServiceStation> nextStations) {
		this.currentStation = currentStation;
		this.nextStations = nextStations;
	}

	public ServiceStation getCurrentStation() {
		return currentStation;
	}

	public Collection<ServiceStation> getNextStations() {
		return nextStations;
	}

}
