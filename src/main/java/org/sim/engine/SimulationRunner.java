package org.sim.engine;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import org.sim.assignment.StationAssignment;
import org.sim.generator.EventGenerator;
import org.sim.stat.SimulationStatistics;
import org.sim.stat.SimulationStatisticsFactory;
import org.sim.stat.TestResultsAnalyzer;

@AllArgsConstructor(onConstructor_ = @Inject)
public class SimulationRunner {
	private final int numberOfSimulations;
	private final double simulationTime;
	private final EventGenerator eventGenerator;
	private final StationAssignment stationAssignment;
	private final TestResultsAnalyzer testResultsAnalyzer;

	public void run() {
		for (int i = 0; i < numberOfSimulations; i++) {
			final SimulationStatistics simulationStatistics = SimulationStatisticsFactory.create();
			final SimulationEngine engine = SimulationEngineFactory.create();
			final StationAssignment stationAssignmentCopy = StationAssignment.copyOf(stationAssignment); // do this to
																											// prevent
																											// using
																											// the same
																											// stations
																											// for
																											// different
																											// simulation
																											// runs
			new SingleSimulationRunner(simulationTime, eventGenerator, stationAssignmentCopy, engine,
					simulationStatistics)
					.run();
			testResultsAnalyzer.addResults(simulationStatistics.getSimulationResults());
		}
	}

	public void showResults() {
		testResultsAnalyzer.getResults();
	}
}
