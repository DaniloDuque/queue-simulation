package org.sim.run;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import org.sim.engine.*;
import org.sim.station.assignment.StationAssignment;
import org.sim.generator.EventGenerator;
import org.sim.stat.single.SimulationStatistics;
import org.sim.stat.single.SimulationStatisticsFactory;
import org.sim.stat.multiple.TestResultsRecord;
import org.sim.station.assignment.StationWorkerAssigner;
import org.sim.station.assignment.StationConfiguration;

@AllArgsConstructor(onConstructor_ = @Inject)
public class SimulationRunner {
	private final int numberOfSimulations;
	private final double simulationTime;
	private final EventGenerator eventGenerator;
	private final StationConfiguration stationConfiguration;

	public TestResultsRecord run() {
		final TestResultsRecord testResultsRecord = new TestResultsRecord(stationConfiguration);
		for (int i = 0; i < numberOfSimulations; i++) {
			final SimulationStatistics simulationStatistics = SimulationStatisticsFactory.create();
			final EventQueue eventQueue = new EventQueue();
			final EventFifoManager eventProvider = new EventFifoManager(eventQueue);
			final SimulationEngine engine = SimulationEngineFactory.create(eventProvider);
			final StationAssignment stationAssignment = StationWorkerAssigner.assignWorkers(stationConfiguration,
					simulationStatistics);
			new SingleSimulationRunner(simulationTime, eventGenerator, stationAssignment, eventProvider, engine)
					.run();
			testResultsRecord.addResults(simulationStatistics.getSimulationResults());
		}
		return testResultsRecord;
	}
}
