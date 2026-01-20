package org.sim.engine;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import org.sim.event.EventGenerator;
import org.sim.station.StationWorkflow;

@AllArgsConstructor(onConstructor_ = @Inject)
public class SingleSimulationRunner extends Thread {
	private final double simulationTime;
	private final EventGenerator eventGenerator;
	private final StationWorkflow stationWorkflow;

	@Override
	public void run() {
		final SimulationEngine engine = SimulationEngineFactory.create();
		eventGenerator.generateEventsUntil(simulationTime, stationWorkflow, engine).forEach(engine::schedule);
		engine.run(simulationTime);
	}
}
