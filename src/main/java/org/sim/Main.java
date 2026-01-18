package org.sim;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.sim.engine.SimulationEngine;
import org.sim.event.EventGenerator;
import org.sim.module.SimulationModule;
import org.sim.stat.StatisticsCollector;

public class Main {
	public static void main(String[] args) {
		final Injector injector = Guice.createInjector(new SimulationModule());

		final EventGenerator generator = injector.getInstance(EventGenerator.class);
		final SimulationEngine engine = injector.getInstance(SimulationEngine.class);
		final StatisticsCollector statisticsCollector = injector.getInstance(StatisticsCollector.class);
		final double maximumTime = 3600 * 8.0;

		generator.generateEventsUntil(maximumTime).forEach(engine::schedule);
		engine.run(maximumTime);

		statisticsCollector.printStats();
	}
}
