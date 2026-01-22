package org.sim.engine;

import lombok.NonNull;

public class SimulationEngineFactory {
	public static SimulationEngine create(@NonNull final EventProvider eventProvider) {
		final SimulationClock clock = new SimulationClock();
		return new SimulationEngine(clock, eventProvider);
	}
}
