package org.sim.tester;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sim.assignment.StationWorkflowGenerator;
import org.sim.engine.SimulationRunner;
import org.sim.event.EventGenerator;
import org.sim.stat.TestResultsAnalyzer;
import org.sim.station.StationWorkflow;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Slf4j
@AllArgsConstructor(onConstructor_ = @Inject)
public class CombinationTester {
	private final int numberOfSimulations;
	private final double simulationTime;
	private final EventGenerator eventGenerator;
	private final StationWorkflowGenerator stationWorkflowGenerator;
	private final ExecutorService executor;

	public void run() {
		final List<Future<?>> futures = new ArrayList<>();

		while (stationWorkflowGenerator.hasNext()) {
			final StationWorkflow stationWorkflow = stationWorkflowGenerator.next();
			futures.add(executor.submit(() -> {
				final TestResultsAnalyzer testResultsAnalyzer = new TestResultsAnalyzer(numberOfSimulations);
				new SimulationRunner(numberOfSimulations, simulationTime, eventGenerator,
						stationWorkflow, testResultsAnalyzer).run();
				log.info("Combination finished");
			}));
		}

		for (final Future<?> future : futures) {
			try {
				future.get();
			} catch (Exception e) {
				Thread.currentThread().interrupt();
			}
		}
	}

}
