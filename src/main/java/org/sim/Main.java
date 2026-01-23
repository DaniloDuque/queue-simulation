package org.sim;

import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;
import org.sim.module.Constants;
import org.sim.module.SimulationModule;
import org.sim.optimizer.time.TimeOptimizer;
import org.sim.stat.multiple.TestResult;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Main {
	public static void main(String[] args) {
		final Injector injector = Guice.createInjector(new SimulationModule());
		final TimeOptimizer timeOptimizer = injector.getInstance(TimeOptimizer.class);
		final ExecutorService executor = injector.getInstance(ExecutorService.class);

		try {
			final TestResult best = timeOptimizer.getBestConfigurationForBudget(Constants.BUDGET);
			log.info("=== BEST CONFIGURATION ===");
			log.info("Average served clients: {}", best.averageServedClients());
			log.info("Configuration: {}", best.workerConfiguration());
			log.info("Average wait time: {}", best.averageWaitTime());
			log.info("Min wait time: {}", best.minWaitTime());
			log.info("Max wait time: {}", best.maxWaitTime());
			log.info("Wait time standard deviation: {}", best.waitTimeStdDev());
			executor.shutdown();
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			log.warn("Simulation interrupted, shutting down gracefully");
			Thread.currentThread().interrupt();
			executor.shutdownNow();
		}
	}
}
