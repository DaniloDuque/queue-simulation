package org.sim.optimizer.budget;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.sim.generator.EventGenerator;
import org.sim.generator.WorkerCountGenerator;
import org.sim.optimizer.time.TimeOptimizer;
import org.sim.stat.multiple.TestResult;
import org.sim.station.StationName;
import org.sim.station.assignment.StationSpecification;

import java.util.Optional;
import java.util.concurrent.ExecutorService;

@Slf4j
@AllArgsConstructor(onConstructor_ = @Inject)
public class BudgetOptimizer {
	private final int numberOfSimulations;
	private final double simulationTime;
	private final EventGenerator eventGenerator;
	private final WorkerCountGenerator workerCountGenerator;
	private final ImmutableMap<StationName, StationSpecification> stationSpecifications;
	private final ExecutorService executor;
	private final TimeOptimizer timeOptimizer;

	// Returns the test results of the simulation run with the configuration that
	// minimizes the budget needed to achieve an average wait time of less than
	// "time"
	public TestResult getBestConfigurationForTime(@NonNull final Double time) {
		Double enoughBudget = 20000.0;
		Double insufficientBudget = 1550.0;
		final Double epsilon = 1e2;

		TestResult goodConfig = null;

		while (insufficientBudget + epsilon < enoughBudget) {
			log.info("Budget range: [{}, {}]", insufficientBudget, enoughBudget);
			final Double midBudget = (insufficientBudget + enoughBudget) / 2;
			final Optional<TestResult> resultOptional = timeOptimizer.getBestConfigurationForBudget(midBudget);

			if (resultOptional.isEmpty() || resultOptional.get().averageWaitTime() > time) {
				insufficientBudget = midBudget;
			} else {
				enoughBudget = midBudget;
				goodConfig = resultOptional.get();
			}
		}

		return goodConfig;
	}
}
