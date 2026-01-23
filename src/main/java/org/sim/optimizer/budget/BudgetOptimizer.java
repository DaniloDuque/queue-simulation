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
		Double enoughBudget = 4e4;
		Double insufficientBudget = 22e3;
		final Double epsilon = 1e-1;

		TestResult goodConfig = null;

		while (insufficientBudget + epsilon < enoughBudget) {
			Double midBudget = (insufficientBudget + enoughBudget) / 2;
			TestResult result = timeOptimizer.getBestConfigurationForBudget(midBudget);

			if ((result == null) || (result.averageWaitTime() > time)) {
				insufficientBudget = midBudget;
				log.info("result null");
			} else {
				log.info("result not null");
				enoughBudget = midBudget;
				goodConfig = result;
			}
		}

		return goodConfig;
	}
}
