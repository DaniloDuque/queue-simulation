package org.sim.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.NonNull;
import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.sim.assignment.StationRouter;
import org.sim.distribution.BinomialServiceTimeDistribution;
import org.sim.distribution.ExponentialServiceTimeDistribution;
import org.sim.distribution.GeometricServiceTimeDistribution;
import org.sim.distribution.NormalServiceTimeDistribution;
import org.sim.engine.EventQueue;
import org.sim.engine.SimulationClock;
import org.sim.engine.SimulationEngine;
import org.sim.event.EventGenerator;
import org.sim.model.Clients;
import org.sim.stat.StatisticsCollector;
import org.sim.station.StationName;
import org.sim.station.StationSpecification;
import org.sim.tester.CombinationTester;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class SimulationModule extends AbstractModule {

	@Provides
	@Singleton
	Clients provideClients() {
		return new Clients(new HashMap<>());
	}

	@Provides
	@Singleton
	StatisticsCollector provideStatisticsCollector(@NonNull final Clients clients) {
		return new StatisticsCollector(new LinkedList<>(), clients);
	}

	@Provides
	@Singleton
	ExponentialDistribution provideArrivalDistribution() {
		return new ExponentialDistribution(1.0 / Constants.CLIENT_ARRIVAL_RATE_PER_SECOND);
	}

	@Provides
	@Singleton
	SimulationClock provideSimulationClock() {
		return new SimulationClock();
	}

	@Provides
	@Singleton
	EventQueue provideEventQueue() {
		return new EventQueue();
	}

	@Provides
	@Singleton
	SimulationEngine provideSimulationEngine(SimulationClock clock, EventQueue eventQueue) {
		return new SimulationEngine(clock, eventQueue);
	}

	@Provides
	@Singleton
	EventGenerator provideEventGenerator(ExponentialDistribution arrivalDistribution) {
		return new EventGenerator(arrivalDistribution);
	}

	@Provides
	Map<StationName, StationSpecification> provideStationSpecifications(
			@NonNull final StatisticsCollector statisticsCollector) {
		final StationSpecification cashierStationSpecification = new StationSpecification(StationName.CASHIER,
				new ExponentialServiceTimeDistribution(Constants.CASHIER_STATION_MEAN), new LinkedList<>(),
				statisticsCollector);
		final StationSpecification drinksStationSpecification = new StationSpecification(StationName.DRINKS,
				new ExponentialServiceTimeDistribution(Constants.DRINKS_STATION_MEAN), new LinkedList<>(),
				statisticsCollector);
		final StationSpecification frierStationSpecification = new StationSpecification(StationName.FRIER,
				new NormalServiceTimeDistribution(Constants.FRIER_STATION_MEAN, Constants.FRIER_STATION_STD),
				new LinkedList<>(), statisticsCollector);
		final StationSpecification desertStationSpecification = new StationSpecification(StationName.DESERT,
				new BinomialServiceTimeDistribution(Constants.DESERT_STATION_N, Constants.DESERT_STATION_P),
				new LinkedList<>(), statisticsCollector);
		final StationSpecification chickenStationSpecification = new StationSpecification(StationName.CHICKEN,
				new GeometricServiceTimeDistribution(Constants.CHICKEN_STATION_P), new LinkedList<>(),
				statisticsCollector);

		return Map.of(
				StationName.CASHIER, cashierStationSpecification,
				StationName.DRINKS, drinksStationSpecification,
				StationName.FRIER, frierStationSpecification,
				StationName.DESERT, desertStationSpecification,
				StationName.CHICKEN, chickenStationSpecification);
	}

	@Provides
	@Singleton
	StationRouter provideStationRouter(@NonNull final Map<StationName, StationSpecification> stationSpecifications) {
		return new StationRouter(Constants.TOTAL_NUMBER_OF_WORKERS, stationSpecifications);
	}

	@Provides
	@Singleton
	CombinationTester provideCombinationTester(@NonNull final EventGenerator eventGenerator,
			@NonNull final SimulationEngine engine, @NonNull final StationRouter stationRouter) {
		return new CombinationTester(Constants.NUMBER_OF_SIMULATIONS_PER_COMBINATION,
				Constants.SIMULATION_TIME_IN_SECONDS, eventGenerator, engine, stationRouter);
	}
}
