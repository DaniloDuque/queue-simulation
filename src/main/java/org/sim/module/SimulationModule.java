package org.sim.module;

import com.google.common.collect.ImmutableMap;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.NonNull;
import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.sim.assignment.StationAssignmentService;
import org.sim.distribution.BinomialServiceTimeDistribution;
import org.sim.distribution.ExponentialServiceTimeDistribution;
import org.sim.distribution.GeometricServiceTimeDistribution;
import org.sim.distribution.NormalServiceTimeDistribution;
import org.sim.generator.CompositionGenerator;
import org.sim.generator.EventGenerator;
import org.sim.station.StationName;
import org.sim.station.StationSpecification;
import org.sim.tester.CombinationTester;

import java.util.LinkedList;
import java.util.concurrent.*;

public class SimulationModule extends AbstractModule {

	@Provides
	ImmutableMap<StationName, StationSpecification> provideStationSpecifications() {
		final StationSpecification cashierStationSpecification = new StationSpecification(
				new ExponentialServiceTimeDistribution(Constants.CASHIER_STATION_MEAN), new LinkedList<>());
		final StationSpecification drinksStationSpecification = new StationSpecification(
				new ExponentialServiceTimeDistribution(Constants.DRINKS_STATION_MEAN), new LinkedList<>());
		final StationSpecification frierStationSpecification = new StationSpecification(
				new NormalServiceTimeDistribution(Constants.FRIER_STATION_MEAN, Constants.FRIER_STATION_STD),
				new LinkedList<>());
		final StationSpecification desertStationSpecification = new StationSpecification(
				new BinomialServiceTimeDistribution(Constants.DESERT_STATION_N, Constants.DESERT_STATION_P),
				new LinkedList<>());
		final StationSpecification chickenStationSpecification = new StationSpecification(
				new GeometricServiceTimeDistribution(Constants.CHICKEN_STATION_P), new LinkedList<>());

		return ImmutableMap.of(
				StationName.CASHIER, cashierStationSpecification,
				StationName.DRINKS, drinksStationSpecification,
				StationName.FRIER, frierStationSpecification,
				StationName.DESERT, desertStationSpecification,
				StationName.CHICKEN, chickenStationSpecification);
	}

	@Provides
	@Singleton
	ExponentialDistribution provideArrivalDistribution() {
		return new ExponentialDistribution(1.0 / Constants.CLIENT_ARRIVAL_RATE_PER_SECOND);
	}

	@Provides
	@Singleton
	EventGenerator provideEventGenerator(@NonNull final ExponentialDistribution arrivalDistribution) {
		return new EventGenerator(arrivalDistribution);
	}

	@Provides
	@Singleton
	ExecutorService provideExecutorService() {
		int threads = Constants.THREAD_POOL_SIZE;
		BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(Constants.THREAD_POOL_SIZE * 3);

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
	CompositionGenerator provideCompositionGenerator() {
		return new CompositionGenerator(Constants.TOTAL_NUMBER_OF_WORKERS, Constants.NUMBER_OF_STATIONS);
	}

	@Provides
	@Singleton
	StationAssignmentService provideStationAssignmentService(@NonNull final CompositionGenerator compositionGenerator,
			@NonNull final ImmutableMap<StationName, StationSpecification> stationSpecifications) {
		return new StationAssignmentService(compositionGenerator, stationSpecifications);
	}

	@Provides
	@Singleton
	CombinationTester provideCombinationTester(@NonNull final EventGenerator eventGenerator,
			@NonNull final StationAssignmentService stationAssignmentService,
			@NonNull final ExecutorService executor) {
		return new CombinationTester(Constants.NUMBER_OF_SIMULATIONS_PER_COMBINATION,
				Constants.SIMULATION_TIME_IN_SECONDS, eventGenerator, stationAssignmentService, executor);
	}
}
