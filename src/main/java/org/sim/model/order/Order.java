package org.sim.model.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.sim.station.ServiceStation;
import org.sim.station.StationWorkflow;

import java.util.Collection;

@Getter
@RequiredArgsConstructor
public class Order {
	private final int id;
	private final double startTime;
	private final StationWorkflow stationWorkflow;

	@Setter
	private double endTime;

	public ServiceStation getCurrentStation() {
		return stationWorkflow.getCurrentStation();
	}

	public Collection<StationWorkflow> getChildStationWorkflows() {
		return stationWorkflow.getNextStations();
	}
}
