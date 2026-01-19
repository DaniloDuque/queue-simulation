package org.sim.order;

import lombok.Setter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sim.station.ServiceStation;
import org.sim.station.StationWorkflow;

import java.util.Collection;

@Getter
@RequiredArgsConstructor
public class Order {
	private final long id;
	private final StationWorkflow stationWorkflow;

	@Setter
	private double queueStartTime;
	private double totalWaitingTimeInQueue = 0.0;

	@Setter
	private double serviceStartTime;
	private double totalWaitingTimeInService = 0.0;

	public void addWaitingTimeInQueue(final double waitingTimeInQueue) {
		totalWaitingTimeInQueue += waitingTimeInQueue;
	}

	public void addWaitingTimeInService(final double waitingTimeInService) {
		totalWaitingTimeInService += waitingTimeInService;
	}

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
