package org.sim;

import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;
import org.sim.module.SimulationModule;
import org.sim.tester.CombinationTester;

@Slf4j
public class Main {
	public static void main(String[] args) throws InterruptedException {
		final Injector injector = Guice.createInjector(new SimulationModule());
		final CombinationTester combinationTester = injector.getInstance(CombinationTester.class);
		// final SimulationStatistics simulationStatistics =
		// injector.getInstance(SimulationStatistics.class);
		combinationTester.run();

	}
}
