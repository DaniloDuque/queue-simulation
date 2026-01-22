package org.sim.model.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.sim.station.Station;
import org.sim.station.router.StationWorkflow;

import java.util.Collection;

@Getter
@RequiredArgsConstructor
public class Order {
	private final int id;
	private final double startTime;
	private final StationWorkflow stationWorkflow;

	@Setter
	private double endTime;

	public Station getCurrentStation() {
		return stationWorkflow.getCurrentStation();
	}

	public Collection<StationWorkflow> getChildStationWorkflows() {
		return stationWorkflow.getNextStations();
	}
}
