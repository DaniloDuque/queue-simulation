package org.sim.scenario;

import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;
import org.sim.module.SimulationModule;
import org.sim.module.SimulationConfiguration;
import org.sim.optimizer.budget.BudgetOptimizer;
import org.sim.optimizer.OptimizerResult;
import org.sim.optimizer.time.TimeOptimizer;

import java.util.Collection;
import java.util.List;

@Slf4j
public class ScenarioD implements Scenario {
	private static final double REDUCED_CASHIER_TIME = 2.0; // 2 minutes
	private static final double MAX_WAIT_TIME = 3.0 * 60; // 3 minutes in seconds
	private static final double BUDGET_2000 = 2000.0;
	private static final double BUDGET_3000 = 3000.0;

	@Override
	public Collection<OptimizerResult> test() {
		log.info("Running Scenario D with reduced cashier service time: {} minutes", REDUCED_CASHIER_TIME);

		// Create configuration with reduced cashier time
		final SimulationConfiguration config = SimulationConfiguration.builder()
				.cashierStationMean(REDUCED_CASHIER_TIME)
				.build();

		// Create injector with parameterized module
		final Injector injector = Guice.createInjector(new SimulationModule(config));

		final BudgetOptimizer budgetOptimizer = injector.getInstance(BudgetOptimizer.class);
		final TimeOptimizer timeOptimizer = injector.getInstance(TimeOptimizer.class);

		// Test minimum budget for ≤3 min wait time
		final OptimizerResult minBudgetResult = budgetOptimizer.getTop3ConfigurationsForTime(MAX_WAIT_TIME);
		log.info("Minimum budget for ≤3 min: ${}", minBudgetResult.getOptimizedValues());

		// Test $2000 budget
		final OptimizerResult budget2000Results = timeOptimizer.getTop3ConfigurationsForBudget(BUDGET_2000);
		log.info("$2000 budget - Best wait time: {:.2f}s",
				budget2000Results.getBestResults().getFirst().averageWaitTime());

		// Test $3000 budget
		final OptimizerResult budget3000Results = timeOptimizer.getTop3ConfigurationsForBudget(BUDGET_3000);
		log.info("$3000 budget - Best wait time: {:.2f}s",
				budget3000Results.getBestResults().getFirst().averageWaitTime());

		return List.of(minBudgetResult, budget2000Results, budget3000Results);
	}
}
