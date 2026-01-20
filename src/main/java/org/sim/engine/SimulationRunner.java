package org.sim.engine;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import org.sim.event.EventGenerator;
import org.sim.model.Clients;
import org.sim.stat.SimulationStatistics;
import org.sim.stat.TestResultsAnalyzer;
import org.sim.station.StationWorkflow;

import java.util.HashMap;
import java.util.LinkedList;

@AllArgsConstructor(onConstructor_ = @Inject)
public class SimulationRunner {
	private final int numberOfSimulations;
	private final double simulationTime;
	private final EventGenerator eventGenerator;
	private final StationWorkflow stationWorkflow;
	private final TestResultsAnalyzer testResultsAnalyzer;

	public void run() {
		for (int i = 0; i < numberOfSimulations; i++) {
			final Clients clients = new Clients(new HashMap<>());
			final SimulationStatistics simulationStatistics = new SimulationStatistics(new LinkedList<>(), clients);
			final SimulationEngine engine = SimulationEngineFactory.create();
			final StationWorkflow freshWorkflow = stationWorkflow.deepCopy();
			new SingleSimulationRunner(simulationTime, eventGenerator, freshWorkflow, engine, simulationStatistics)
					.run();
			testResultsAnalyzer.addResults(simulationStatistics.getSimulationResults());
		}
	}

	public void showResults() {
		testResultsAnalyzer.getResults();
	}
}
