package org.sim;

import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;
import org.sim.module.SimulationModule;
import org.sim.tester.CombinationTester;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Main {
	public static void main(String[] args) throws InterruptedException {
		final Injector injector = Guice.createInjector(new SimulationModule());
		final CombinationTester combinationTester = injector.getInstance(CombinationTester.class);
		final ExecutorService executor = injector.getInstance(ExecutorService.class);

		combinationTester.run();

		executor.shutdown();
		executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
	}
}
