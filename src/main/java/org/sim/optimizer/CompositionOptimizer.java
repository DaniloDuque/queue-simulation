package org.sim.optimizer;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sim.generator.WorkerCountWithBudgetGenerator;
import org.sim.run.SimulationRunner;
import org.sim.station.assignment.StationSpecification;
import org.sim.generator.EventGenerator;
import org.sim.stat.multiple.TestResult;
import org.sim.stat.multiple.TestResultsRecord;
import org.sim.station.StationName;
import org.sim.station.assignment.StationConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Slf4j
@AllArgsConstructor(onConstructor_ = @Inject)
public class CompositionOptimizer {
	private final int numberOfSimulations;
	private final double simulationTime;
	private final EventGenerator eventGenerator;
	private final WorkerCountWithBudgetGenerator workerCountWithBudgetGenerator;
	private final ImmutableMap<StationName, StationSpecification> stationSpecifications;
	private final ExecutorService executor;

	public void run() {
		final List<Future<TestResult>> futures = new ArrayList<>();

		while (workerCountWithBudgetGenerator.hasNext()) {
			final ImmutableMap<StationName, Integer> workerCountPerStation = workerCountWithBudgetGenerator.next();
			final StationConfiguration stationConfiguration = new StationConfiguration(workerCountPerStation,
					stationSpecifications);
			futures.add(executor.submit(() -> {
				final TestResultsRecord testResultsRecord = new SimulationRunner(numberOfSimulations,
						simulationTime, eventGenerator,
						stationConfiguration).run();
				return testResultsRecord.getResults();
			}));
		}

		final List<TestResult> results = new ArrayList<>();
		for (final Future<TestResult> future : futures) {
			try {
				results.add(future.get());
			} catch (Exception e) {
				log.error("Error while running simulation", e);
				Thread.currentThread().interrupt();
			}
		}

		final TestResult best = results.stream().min(TestResult::compareTo).orElse(null);
		if (best != null) {
			log.info("=== BEST CONFIGURATION ===");
			log.info("Average served clients: {}", best.averageServedClients());
			log.info("Configuration: {}", best.workerConfiguration());
			log.info("Average wait time: {}", best.averageWaitTime());
			log.info("Min wait time: {}", best.minWaitTime());
			log.info("Max wait time: {}", best.maxWaitTime());
			log.info("Wait time standard deviation: {}", best.waitTimeStdDev());
		}
	}
}
