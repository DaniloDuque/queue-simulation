package org.sim.tester;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sim.assignment.StationWorkflowGenerator;
import org.sim.engine.SimulationRunner;
import org.sim.event.EventGenerator;
import org.sim.stat.ConfigurationResult;
import org.sim.stat.ConfigurationSummary;
import org.sim.stat.TestResultsAnalyzer;
import org.sim.station.StationName;
import org.sim.station.StationWorkflow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
		final List<Future<ConfigurationResult>> futures = new ArrayList<>();

		while (stationWorkflowGenerator.hasNext()) {
			final StationWorkflow stationWorkflow = stationWorkflowGenerator.next();
			final Map<StationName, Integer> workerConfig = stationWorkflowGenerator.getCurrentWorkerConfiguration();
			futures.add(executor.submit(() -> {
				final TestResultsAnalyzer testResultsAnalyzer = new TestResultsAnalyzer();
				new SimulationRunner(numberOfSimulations, simulationTime, eventGenerator,
						stationWorkflow, testResultsAnalyzer).run();
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
			log.info("Average served clients: {}", best.averageServedClients());
			log.info("Average wait time: {}", best.averageWaitTime());
			log.info("Min wait time: {}", best.minWaitTime());
			log.info("Max wait time: {}", best.maxWaitTime());
			log.info("Wait time standard deviation: {}", best.waitTimeStdDev());
			log.info("Min served clients: {}", best.minServedClients());
			log.info("Max served clients: {}", best.maxServedClients());
			log.info("Served clients standard deviation: {}", best.servedClientsStdDev());
		}
	}
}
