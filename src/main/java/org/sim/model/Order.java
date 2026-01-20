package org.sim.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sim.station.ServiceStation;
import org.sim.station.StationWorkflow;

import java.util.Collection;

@Getter
@RequiredArgsConstructor
public class Order {
	private final int id;
	private final StationWorkflow stationWorkflow;

	private final double startTime;
	private final double endTime;

	public int getOrderSizeForCurrentStation() {
		return OrderSizeGenerator.generate();
	}

	public ServiceStation getCurrentStation() {
		return stationWorkflow.getCurrentStation();
	}

	public Collection<StationWorkflow> getChildStationWorkflows() {
		return stationWorkflow.getNextStations();
	}
}
