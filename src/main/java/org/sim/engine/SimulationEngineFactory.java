package org.sim.engine;

public class SimulationEngineFactory {
	public static SimulationEngine create() {
		final SimulationClock clock = new SimulationClock();
		final EventQueue eventQueue = new EventQueue();
		return new SimulationEngine(clock, eventQueue);
	}
}
