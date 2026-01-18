package org.sim.event;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import org.sim.engine.SimulationEngine;
import org.sim.client.Client;
import org.sim.station.ServiceStation;

@AllArgsConstructor(onConstructor_ = @Inject)
public class ArrivalEvent implements Event {

	private final double arrivalTime;
	private final Client client;
	private final ServiceStation station;
	private final SimulationEngine engine;

	@Override
	public double time() {
		return arrivalTime;
	}

	@Override
	public void process() {
		station.arrive(client, engine);
	}
}
