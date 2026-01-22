package org.sim.event;

import lombok.AllArgsConstructor;
import org.sim.engine.EventScheduler;
import org.sim.model.order.Order;

@AllArgsConstructor
public class ArrivalEvent implements Event {
	private final double arrivalTime;
	private final Order order;
	private final EventScheduler eventScheduler;

	@Override
	public double time() {
		return arrivalTime;
	}

	@Override
	public void process() {
		order.getCurrentStation().arrive(order, eventScheduler, arrivalTime);
	}
}
