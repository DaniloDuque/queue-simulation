package org.sim.scenario;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.sim.module.SimulationModule;
import org.sim.module.SimulationConfiguration;
import org.sim.optimizer.budget.BudgetOptimizer;
import org.sim.optimizer.OptimizerResult;

import java.util.Collection;
import java.util.List;

public class ScenarioE implements Scenario {
	private static final double CHICKEN_PROBABILITY = 0.5; // 50% instead of 30%
	private static final double MAX_WAIT_TIME = 3.0 * 60; // 3 minutes in seconds

	@Override
	public Collection<OptimizerResult> test() {
		// Create configuration with increased chicken probability
		final SimulationConfiguration config = SimulationConfiguration.builder()
				.chickenProb(CHICKEN_PROBABILITY)
				.build();

		// Create injector with parameterized module
		final Injector injector = Guice.createInjector(new SimulationModule(config));

		final BudgetOptimizer budgetOptimizer = injector.getInstance(BudgetOptimizer.class);

		return List.of(budgetOptimizer.getTop3ConfigurationsForTime(MAX_WAIT_TIME));
	}
}
