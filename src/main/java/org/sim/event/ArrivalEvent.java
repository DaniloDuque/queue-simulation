package org.sim.event;

import lombok.AllArgsConstructor;
import org.sim.engine.SimulationEngine;
import org.sim.model.order.Order;
import org.sim.stat.single.SimulationStatistics;

@AllArgsConstructor
public class ArrivalEvent implements Event {
	private final double arrivalTime;
	private final Order order;
	private final SimulationEngine engine;
	private final SimulationStatistics simulationStatistics;

	@Override
	public double time() {
		return arrivalTime;
	}

	@Override
	public void process() {
		order.getCurrentStation().arrive(order, engine, simulationStatistics);
	}
}
