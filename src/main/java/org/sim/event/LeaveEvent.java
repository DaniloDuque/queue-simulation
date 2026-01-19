package org.sim.event;

import lombok.AllArgsConstructor;
import org.sim.engine.*;
import org.sim.order.Order;

@AllArgsConstructor
public class LeaveEvent implements Event {

	private final double departureTime;
	private final Order order;
	private final SimulationEngine engine;

	@Override
	public double time() {
		return departureTime;
	}

	@Override
	public void process() {
		order.getCurrentStation().leave(order, engine);
	}
}
