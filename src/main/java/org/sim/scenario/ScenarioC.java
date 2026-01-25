package org.sim.scenario;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.sim.module.SimulationConfiguration;
import org.sim.module.SimulationModule;
import org.sim.optimizer.OptimizerResult;
import org.sim.optimizer.time.TimeOptimizer;

import java.util.Collection;
import java.util.List;

public class ScenarioC implements Scenario {
	private static final double BUDGET = 3000.0;

	public Collection<OptimizerResult> test() {
		final SimulationConfiguration defaultConfig = SimulationConfiguration.builder().build();
		final Injector injector = Guice.createInjector(new SimulationModule(defaultConfig));
		final TimeOptimizer timeOptimizer = injector.getInstance(TimeOptimizer.class);

		return List.of(timeOptimizer.getTop3ConfigurationsForBudget(BUDGET));
	}
}
