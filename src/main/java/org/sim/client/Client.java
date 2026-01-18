package org.sim.client;

import java.util.Queue;

import lombok.Setter;
import org.sim.station.ServiceStation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Client {
	private final long id;
	private final Queue<ServiceStation> stationSequence;

	private double totalWaitingTimeInQueue = 0.0;
	@Setter
	private double queueStartTime;
	private double totalWaitingTimeInService = 0.0;
	@Setter
	private double serviceStartTime;

	public void addWaitingTimeInQueue(final double waitingTimeInQueue) {
		totalWaitingTimeInQueue += waitingTimeInQueue;
	}

	public void addWaitingTimeInService(final double waitingTimeInService) {
		totalWaitingTimeInService += waitingTimeInService;
	}

	public int getOrderSizeForCurrentStation() {
		return OrderSizeGenerator.generate();
	}
}
