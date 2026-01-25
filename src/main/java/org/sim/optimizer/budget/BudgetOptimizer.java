package org.sim.optimizer.budget;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.sim.optimizer.OptimizerResult;
import org.sim.optimizer.time.TimeOptimizer;
import org.sim.stat.multiple.TestResult;
import org.sim.module.Constants;
import java.util.Collections;

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
		List<Double> goodBudgets = new ArrayList<>();

		while (insufficientBudget + epsilon < enoughBudget) {
			log.info("Budget range: [{}, {}]", insufficientBudget, enoughBudget);
			final Double midBudget = (insufficientBudget + enoughBudget) / 2;
			final OptimizerResult optimizerResult = timeOptimizer.getTop3ConfigurationsForBudget(midBudget);

			List<TestResult> validResults = optimizerResult.getBestResults().stream()
					.filter(result -> result.averageWaitTime() <= time)
					.toList();

			if (validResults.isEmpty()) {
				insufficientBudget = midBudget;
			} else {
				enoughBudget = midBudget;

				goodConfigs.addAll(validResults);
				goodBudgets.addAll(Collections.nCopies(validResults.size(), enoughBudget));
			}
		}

		return new OptimizerResult(
				goodBudgets.subList(goodBudgets.size() - Constants.NUMBER_OF_COMBINATIONS_EXPECTED, goodBudgets.size()),
				goodConfigs.subList(goodConfigs.size() - Constants.NUMBER_OF_COMBINATIONS_EXPECTED,
						goodConfigs.size()));
	}
}
