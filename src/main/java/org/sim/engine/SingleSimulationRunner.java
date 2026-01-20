package org.sim.engine;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import org.sim.event.EventGenerator;
import org.sim.stat.SimulationStatistics;
import org.sim.station.StationWorkflow;

@AllArgsConstructor(onConstructor_ = @Inject)
public class SingleSimulationRunner {
	private final double simulationTime;
	private final EventGenerator eventGenerator;
	private final StationWorkflow stationWorkflow;
	private final SimulationEngine engine;
	private final SimulationStatistics simulationStatistics;

	public void run() {
		eventGenerator.generateEventsUntil(simulationTime, stationWorkflow, engine, simulationStatistics)
				.forEach(engine::schedule);
		engine.run(simulationTime);
	}
}
