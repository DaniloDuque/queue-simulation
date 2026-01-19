package org.sim.station;

import lombok.Getter;
import lombok.NonNull;

import java.util.Collection;

@Getter
public class StationWorkflow {
	private final ServiceStation currentStation;
	private final Collection<StationWorkflow> nextStations;

	public StationWorkflow(@NonNull final ServiceStation currentStation,
			@NonNull final Collection<StationWorkflow> nextStations) {
		this.currentStation = currentStation;
		this.nextStations = nextStations;
	}
}
