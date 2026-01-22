package org.sim;

import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;
import org.sim.module.SimulationModule;
import org.sim.optimizer.CompositionOptimizer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Main {
	public static void main(String[] args) {
		final Injector injector = Guice.createInjector(new SimulationModule());
		final CompositionOptimizer compositionOptimizer = injector.getInstance(CompositionOptimizer.class);
		final ExecutorService executor = injector.getInstance(ExecutorService.class);

		try {
			compositionOptimizer.run();
			executor.shutdown();
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			log.warn("Simulation interrupted, shutting down gracefully");
			Thread.currentThread().interrupt();
			executor.shutdownNow();
		}
	}
}
