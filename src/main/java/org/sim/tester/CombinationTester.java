package org.sim.tester;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sim.assignment.StationAssignment;
import org.sim.assignment.StationAssignmentService;
import org.sim.engine.SimulationRunner;
import org.sim.generator.EventGenerator;
import org.sim.stat.ConfigurationResult;
import org.sim.stat.ConfigurationSummary;
import org.sim.stat.TestResultsAnalyzer;
import org.sim.station.StationName;

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
	private final StationAssignmentService stationAssignmentService;
	private final ExecutorService executor;

	public void run() {
		final List<Future<ConfigurationResult>> futures = new ArrayList<>();

		while (stationAssignmentService.hasNext()) {
			final StationAssignment stationAssignment = stationAssignmentService.next();
			final ImmutableMap<StationName, Integer> workerConfig = stationAssignment.getWorkerCounts();
			futures.add(executor.submit(() -> {
				final TestResultsAnalyzer testResultsAnalyzer = new TestResultsAnalyzer();
				new SimulationRunner(numberOfSimulations, simulationTime, eventGenerator,
						stationAssignment, testResultsAnalyzer).run();
				final ConfigurationSummary summary = testResultsAnalyzer.getResults();
				return new ConfigurationResult(workerConfig,
						summary.averageWaitTime(), summary.averageServedClients(),
						summary.minWaitTime(), summary.maxWaitTime(), summary.waitTimeStdDev(),
						summary.minServedClients(), summary.maxServedClients(), summary.servedClientsStdDev());
			}));
		}

		final List<ConfigurationResult> results = new ArrayList<>();
		for (final Future<ConfigurationResult> future : futures) {
			try {
				results.add(future.get());
			} catch (Exception e) {
				Thread.currentThread().interrupt();
			}
		}

		final ConfigurationResult best = results.stream().min(ConfigurationResult::compareTo).orElse(null);
		if (best != null) {
			log.info("=== BEST CONFIGURATION ===");
			log.info("Configuration: {}", best.workerConfiguration());
			log.info("Average wait time: {}", best.averageWaitTime());
			log.info("Min wait time: {}", best.minWaitTime());
			log.info("Max wait time: {}", best.maxWaitTime());
			log.info("Wait time standard deviation: {}", best.waitTimeStdDev());

			// Add utilization metrics
			final var utilizationRates = org.sim.stat.UtilizationCalculator
					.calculateUtilizationRates(best.workerConfiguration());
			final boolean isStable = org.sim.stat.UtilizationCalculator.isSystemStable(best.workerConfiguration());

			log.info("=== UTILIZATION RATES ===");
			utilizationRates.forEach((station, rate) -> log.info("{}: ρ = {}", station, String.format("%.3f", rate)));
			log.info("System stable (all ρ < 0.8): {}", isStable);
		}
	}
}
