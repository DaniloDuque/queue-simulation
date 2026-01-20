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

	// @Override
	// public void run() {
	// List<Future<?>> futures = new ArrayList<>();
	//
	// for (int i = 0; i < numberOfSimulations; i++) {
	// futures.add(executor.submit(() -> {
	// // I want to run everything inside this in a syncronized block (sequentially)
	// final Clients clients = new Clients(new HashMap<>());
	// final SimulationStatistics simulationStatistics = new
	// SimulationStatistics(new LinkedList<>(), clients);
	// final SimulationEngine engine = SimulationEngineFactory.create();
	// new SingleSimulationRunner(simulationTime, eventGenerator, stationWorkflow,
	// engine,
	// simulationStatistics).run();
	// testResultsAnalyzer.addResults(simulationStatistics.getSimulationResults());
	// }));
	// }
	//
	// // Wait for completion
	// for (Future<?> future : futures) {
	// try {
	// future.get();
	// } catch (Exception e) {
	// Thread.currentThread().interrupt();
	// }
	// }
	//
	// executor.shutdown();
	// try {
	// executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
	// } catch (InterruptedException e) {
	// Thread.currentThread().interrupt();
	// }
	// }

	public void run() {
		for (int i = 0; i < numberOfSimulations; i++) {
			final Clients clients = new Clients(new HashMap<>());
			final SimulationStatistics simulationStatistics = new SimulationStatistics(new LinkedList<>(), clients);
			final SimulationEngine engine = SimulationEngineFactory.create();
			new SingleSimulationRunner(simulationTime, eventGenerator, stationWorkflow,
					engine,
					simulationStatistics).run();
			testResultsAnalyzer.addResults(simulationStatistics.getSimulationResults());
		}
	}

	public void showResults() {
		testResultsAnalyzer.showResults();
	}
}
