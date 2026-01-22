package org.sim.station.router;

import lombok.Getter;
import lombok.NonNull;
import org.sim.station.Station;

import java.util.Collection;

@Getter
public class StationWorkflow {
	private final Station currentStation;
	private final Collection<StationWorkflow> nextStations;

	public StationWorkflow(@NonNull final Station currentStation,
			@NonNull final Collection<StationWorkflow> nextStations) {
		this.currentStation = currentStation;
		this.nextStations = nextStations;
	}
}
