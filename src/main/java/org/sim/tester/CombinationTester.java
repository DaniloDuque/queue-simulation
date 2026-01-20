package org.sim.tester;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import org.sim.assignment.StationRouter;
import org.sim.engine.SimulationEngine;
import org.sim.engine.SimulationRunner;
import org.sim.event.EventGenerator;
import org.sim.station.StationWorkflow;

@AllArgsConstructor(onConstructor_ = @Inject)
public class CombinationTester {
	private final int numberOfSimulations;
	private final double simulationTime;
	private final EventGenerator eventGenerator;
	private final SimulationEngine engine;
	private final StationRouter stationRouter;

	public void run() {
		while (stationRouter.hasNext()) {
			final StationWorkflow stationWorkflow = stationRouter.next();
			eventGenerator.generateEventsUntil(simulationTime, stationWorkflow, engine).forEach(engine::schedule);
			new SimulationRunner(numberOfSimulations, simulationTime, eventGenerator, engine, stationWorkflow).start();
		}
	}
}
