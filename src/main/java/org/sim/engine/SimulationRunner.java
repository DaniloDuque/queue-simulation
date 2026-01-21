package org.sim.engine;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import org.sim.event.EventGenerator;
import org.sim.stat.SimulationStatistics;
import org.sim.stat.SimulationStatisticsFactory;
import org.sim.stat.TestResultsAnalyzer;
import org.sim.station.StationWorkflow;

@AllArgsConstructor(onConstructor_ = @Inject)
public class SimulationRunner {
	private final int numberOfSimulations;
	private final double simulationTime;
	private final EventGenerator eventGenerator;
	private final StationWorkflow stationWorkflow;
	private final TestResultsAnalyzer testResultsAnalyzer;

	public void run() {
		for (int i = 0; i < numberOfSimulations; i++) {
			final SimulationStatistics simulationStatistics = SimulationStatisticsFactory.create();
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
