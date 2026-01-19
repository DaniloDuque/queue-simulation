package org.sim.event;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import org.sim.engine.SimulationEngine;
import org.sim.order.Order;

@AllArgsConstructor(onConstructor_ = @Inject)
public class ArrivalEvent implements Event {

	private final double arrivalTime;
	private final Order order;
	private final SimulationEngine engine;

	@Override
	public double time() {
		return arrivalTime;
	}

	@Override
	public void process() {
		order.getCurrentStation().arrive(order, engine);
	}
}
