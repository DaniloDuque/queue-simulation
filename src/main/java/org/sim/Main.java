package org.sim;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.sim.module.SimulationModule;
import org.sim.tester.CombinationTester;

public class Main {
	public static void main(String[] args) {
		final Injector injector = Guice.createInjector(new SimulationModule());
		final CombinationTester combinationTester = injector.getInstance(CombinationTester.class);
		combinationTester.run();
	}
}
