package org.sim.tester;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import org.sim.assignment.StationWorkflowGenerator;
import org.sim.engine.SimulationRunner;
import org.sim.event.EventGenerator;
import org.sim.station.StationWorkflow;

@AllArgsConstructor(onConstructor_ = @Inject)
public class CombinationTester {
	private final int numberOfSimulations;
	private final double simulationTime;
	private final EventGenerator eventGenerator;
	private final StationWorkflowGenerator stationWorkflowGenerator;

	public void run() throws InterruptedException {
		while (stationWorkflowGenerator.hasNext()) {
			final StationWorkflow stationWorkflow = stationWorkflowGenerator.next();
			final SimulationRunner runner = new SimulationRunner(numberOfSimulations, simulationTime, eventGenerator,
					stationWorkflow);
			runner.start();
			runner.join();
			break;
		}
	}
}
