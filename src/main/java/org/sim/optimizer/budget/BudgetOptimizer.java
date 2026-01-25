package org.sim.optimizer.budget;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.sim.optimizer.time.TimeOptimizer;
import org.sim.stat.multiple.TestResult;

import java.util.List;
import java.util.ArrayList;

@Slf4j
@AllArgsConstructor(onConstructor_ = @Inject)
public class BudgetOptimizer {
	private final TimeOptimizer timeOptimizer;

	// Returns the test results of the simulation run with the configuration that
	// minimizes the budget needed to achieve an average wait time of less than
	// "time"
	public OptimizerResult getTop3ConfigurationsForTime(@NonNull final Double time) {
		Double enoughBudget = 10000.0;
		Double insufficientBudget = 1550.0;
		final Double epsilon = 5e1;

		List<TestResult> goodConfigs = new ArrayList<>();

		while (insufficientBudget + epsilon < enoughBudget) {
			log.info("Budget range: [{}, {}]", insufficientBudget, enoughBudget);
			final Double midBudget = (insufficientBudget + enoughBudget) / 2;
			final List<TestResult> results = timeOptimizer.getTop3ConfigurationsForBudget(midBudget);

			List<TestResult> validResults = results.stream()
					.filter(result -> result.averageWaitTime() <= time)
					.toList();

			if (validResults.size() < 3) {
				insufficientBudget = midBudget;
			} else {
				enoughBudget = midBudget;
				// Only change if all 3 are valid
				goodConfigs = validResults;
			}
		}

		return new OptimizerResult(enoughBudget, goodConfigs);
	}
}
