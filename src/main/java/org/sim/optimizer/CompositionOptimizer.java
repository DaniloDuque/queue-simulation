package org.sim.optimizer;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sim.generator.WorkerCountWithBudgetGenerator;
import org.sim.run.SimulationRunner;
import org.sim.station.assignment.StationSpecification;
import org.sim.generator.EventGenerator;
import org.sim.stat.multiple.ConfigurationResult;
import org.sim.stat.multiple.ConfigurationSummary;
import org.sim.stat.multiple.TestResultsAnalyzer;
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
		final List<Future<ConfigurationResult>> futures = new ArrayList<>();

		while (workerCountWithBudgetGenerator.hasNext()) {
			final ImmutableMap<StationName, Integer> workerCountPerStation = workerCountWithBudgetGenerator.next();
			final StationConfiguration stationConfiguration = new StationConfiguration(workerCountPerStation,
					stationSpecifications);
			futures.add(executor.submit(() -> {
				final TestResultsAnalyzer testResultsAnalyzer = new TestResultsAnalyzer();
				new SimulationRunner(numberOfSimulations, simulationTime, eventGenerator,
						testResultsAnalyzer, stationConfiguration).run();
				final ConfigurationSummary summary = testResultsAnalyzer.getResults();
				return new ConfigurationResult(workerCountPerStation,
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
				log.error("Error while running simulation", e);
				Thread.currentThread().interrupt();
			}
		}

		final ConfigurationResult best = results.stream().min(ConfigurationResult::compareTo).orElse(null);
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
