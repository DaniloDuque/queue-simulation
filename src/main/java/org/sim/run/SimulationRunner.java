package org.sim.run;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.NonNull;
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
	public static TestResultsRecord run(@NonNull final Integer numberOfSimulations,
			@NonNull final Double simulationTime, @NonNull final EventGenerator eventGenerator,
			@NonNull final StationConfiguration stationConfiguration) {

		final TestResultsRecord testResultsRecord = new TestResultsRecord(stationConfiguration);

		for (int i = 0; i < numberOfSimulations; i++) {
			final SimulationStatistics simulationStatistics = SimulationStatisticsFactory.create();
			final EventQueue eventQueue = new EventQueue();
			final EventFifoManager eventProvider = new EventFifoManager(eventQueue);
			final SimulationEngine engine = SimulationEngineFactory.create(eventProvider);
			final StationAssignment stationAssignment = StationWorkerAssigner.assignWorkers(stationConfiguration,
					simulationStatistics);

			SingleSimulationRunner.run(simulationTime, eventGenerator, stationAssignment, eventProvider, engine);
			testResultsRecord.addResults(simulationStatistics.getSimulationResults());
		}
		return testResultsRecord;
	}
}
