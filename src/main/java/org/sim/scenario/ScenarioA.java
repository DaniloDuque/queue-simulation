package org.sim.scenario;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.sim.module.SimulationConfiguration;
import org.sim.module.SimulationModule;
import org.sim.optimizer.budget.BudgetOptimizer;
import org.sim.optimizer.OptimizerResult;

import java.util.Collection;
import java.util.List;

public class ScenarioA implements Scenario {
	private static final double MAX_WAIT_TIME = 3.0 * 60; // 3 minutes in seconds

	public Collection<OptimizerResult> test() {
		final SimulationConfiguration defaultConfig = SimulationConfiguration.builder().build();
		final Injector injector = Guice.createInjector(new SimulationModule(defaultConfig));
		final BudgetOptimizer budgetOptimizer = injector.getInstance(BudgetOptimizer.class);

		return List.of(budgetOptimizer.getTop3ConfigurationsForTime(MAX_WAIT_TIME));
	}
}
