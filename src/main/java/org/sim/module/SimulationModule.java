package org.sim.module;

import com.google.common.collect.ImmutableMap;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.NonNull;
import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.sim.generator.WorkerCountGenerator;
import org.sim.optimizer.time.TimeOptimizer;
import org.sim.station.distribution.ExponentialServiceTimeDistribution;
import org.sim.station.distribution.GeometricServiceTimeDistribution;
import org.sim.station.distribution.NormalServiceTimeDistribution;
import org.sim.generator.CompositionGenerator;
import org.sim.generator.EventGenerator;
import org.sim.station.StationName;
import org.sim.station.StationPrice;
import org.sim.station.assignment.StationSpecification;
import org.sim.optimizer.CompositionOptimizer;
import org.sim.optimizer.budget.BudgetOptimizer;

import java.util.concurrent.*;

public class SimulationModule extends AbstractModule {

	@Provides
	ImmutableMap<StationName, StationSpecification> provideStationSpecifications() {
		final StationSpecification cashierStationSpecification = new StationSpecification(
				new ExponentialServiceTimeDistribution(Constants.CASHIER_STATION_MEAN));
		final StationSpecification drinksStationSpecification = new StationSpecification(
				new ExponentialServiceTimeDistribution(Constants.DRINKS_STATION_MEAN));
		final StationSpecification frierStationSpecification = new StationSpecification(
				new NormalServiceTimeDistribution(Constants.FRYER_STATION_MEAN, Constants.FRYER_STATION_STD));
		final StationSpecification chickenStationSpecification = new StationSpecification(
				new GeometricServiceTimeDistribution(Constants.CHICKEN_STATION_P));

		return ImmutableMap.of(
				StationName.CASHIER, cashierStationSpecification,
				StationName.DRINKS, drinksStationSpecification,
				StationName.FRYER, frierStationSpecification,
				StationName.CHICKEN, chickenStationSpecification);
	}

	@Provides
	@Singleton
	ExponentialDistribution provideArrivalDistribution() {
		return new ExponentialDistribution(1.0 / Constants.CLIENT_ARRIVAL_RATE_PER_SECOND);
	}

	// Generators
	@Provides
	@Singleton
	EventGenerator provideEventGenerator(@NonNull final ExponentialDistribution arrivalDistribution) {
		return new EventGenerator(arrivalDistribution);
	}

	@Provides
	@Singleton
	CompositionGenerator provideCompositionGenerator() {
		return new CompositionGenerator(Constants.TOTAL_NUMBER_OF_WORKERS, Constants.NUMBER_OF_STATIONS);
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
	WorkerCountGenerator provideWorkerCountGenerator(
			@NonNull final CompositionGenerator compositionGenerator) {
		return new WorkerCountGenerator(compositionGenerator);
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
		return new TimeOptimizer(Constants.NUMBER_OF_SIMULATIONS_PER_COMBINATION, Constants.SIMULATION_TIME_IN_SECONDS,
				eventGenerator, stationSpecifications, stationPrice);
	}

	@Provides
	@Singleton
	BudgetOptimizer provideBudgetOptimizer(@NonNull final TimeOptimizer timeOptimizer) {
		return new BudgetOptimizer(timeOptimizer);
	}

	@Provides
	@Singleton
	CompositionOptimizer provideCompositionRunner(@NonNull final EventGenerator eventGenerator,
			@NonNull final WorkerCountGenerator workerCountGenerator,
			@NonNull final ImmutableMap<StationName, StationSpecification> stationSpecifications,
			@NonNull final ExecutorService executor) {
		return new CompositionOptimizer(Constants.NUMBER_OF_SIMULATIONS_PER_COMBINATION,
				Constants.SIMULATION_TIME_IN_SECONDS, eventGenerator, workerCountGenerator,
				stationSpecifications,
				executor);
	}

}
