package org.sim.tester;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import org.sim.assignment.StationWorkflowGenerator;
import org.sim.engine.SimulationRunner;
import org.sim.event.EventGenerator;
import org.sim.stat.TestResultsAnalyzer;
import org.sim.station.StationWorkflow;

@AllArgsConstructor(onConstructor_ = @Inject)
public class CombinationTester {
	private final int numberOfSimulations;
	private final double simulationTime;
	private final EventGenerator eventGenerator;
	private final StationWorkflowGenerator stationWorkflowGenerator;

	public void run() {
		while (stationWorkflowGenerator.hasNext()) {
			final StationWorkflow stationWorkflow = stationWorkflowGenerator.next();
			final TestResultsAnalyzer testResultsAnalyzer = new TestResultsAnalyzer(numberOfSimulations);
			new SimulationRunner(numberOfSimulations, simulationTime, eventGenerator,
					stationWorkflow, testResultsAnalyzer).run();
            break;
		}
	}

}
