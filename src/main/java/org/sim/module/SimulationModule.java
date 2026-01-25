package org.sim.module;

import com.google.common.collect.ImmutableMap;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.sim.generator.EventGenerator;
import org.sim.optimizer.budget.BudgetOptimizer;
import org.sim.optimizer.time.TimeOptimizer;
import org.sim.station.StationName;
import org.sim.station.StationPrice;
import org.sim.station.assignment.StationSpecification;
import org.sim.station.distribution.ExponentialServiceTimeDistribution;
import org.sim.station.distribution.GeometricServiceTimeDistribution;
import org.sim.station.distribution.NormalServiceTimeDistribution;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class SimulationModule extends AbstractModule {
	private final SimulationConfiguration config;

	@Provides
	ImmutableMap<StationName, StationSpecification> provideStationSpecifications() {
		final StationSpecification cashierStationSpecification = new StationSpecification(
				new ExponentialServiceTimeDistribution(config.getCashierStationMean()));
		final StationSpecification drinksStationSpecification = new StationSpecification(
				new ExponentialServiceTimeDistribution(config.getDrinksStationMean()));
		final StationSpecification frierStationSpecification = new StationSpecification(
				new NormalServiceTimeDistribution(config.getFryerStationMean(), config.getFryerStationStd()));
		final StationSpecification chickenStationSpecification = new StationSpecification(
				new GeometricServiceTimeDistribution(config.getChickenStationP()));

		return ImmutableMap.of(
				StationName.CASHIER, cashierStationSpecification,
				StationName.DRINKS, drinksStationSpecification,
				StationName.FRYER, frierStationSpecification,
				StationName.CHICKEN, chickenStationSpecification);
	}

	@Provides
	@Singleton
	ExponentialDistribution provideArrivalDistribution() {
		return new ExponentialDistribution(1.0 / config.getClientArrivalRatePerSecond());
	}

	@Provides
	@Singleton
	EventGenerator provideEventGenerator(@NonNull final ExponentialDistribution arrivalDistribution) {
		return new EventGenerator(arrivalDistribution);
	}

	@Provides
	@Singleton
	StationPrice provideStationPrice() {
		final ImmutableMap<StationName, Double> prices = ImmutableMap.of(StationName.CASHIER,
				Constants.CASHIER_WORKER_PRICE, StationName.DRINKS, Constants.DRINKS_WORKER_PRICE, StationName.FRYER,
				Constants.FRYER_WORKER_PRICE, StationName.CHICKEN, Constants.CHICKEN_WORKER_PRICE);
		return new StationPrice(prices);
	}

	@Provides
	@Singleton
	ExecutorService provideExecutorService() {
		final int threads = Constants.THREAD_POOL_SIZE;
		final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(Constants.THREAD_POOL_SIZE * 3);

		return new ThreadPoolExecutor(
				threads,
				threads,
				0L,
				TimeUnit.MILLISECONDS,
				queue,
				new ThreadPoolExecutor.CallerRunsPolicy());
	}

	@Provides
	@Singleton
	TimeOptimizer provideTimeOptimizer(@NonNull final EventGenerator eventGenerator,
			@NonNull final ImmutableMap<StationName, StationSpecification> stationSpecifications,
			@NonNull final StationPrice stationPrice) {
		return new TimeOptimizer(config.getNumberOfSimulations(), config.getSimulationTime(),
				eventGenerator, stationSpecifications, stationPrice);
	}

	@Provides
	@Singleton
	BudgetOptimizer provideBudgetOptimizer(@NonNull final TimeOptimizer timeOptimizer) {
		return new BudgetOptimizer(timeOptimizer);
	}
}
