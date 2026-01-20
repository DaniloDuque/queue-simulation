package org.sim.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.NonNull;
import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.sim.assignment.StationWorkflowGenerator;
import org.sim.distribution.BinomialServiceTimeDistribution;
import org.sim.distribution.ExponentialServiceTimeDistribution;
import org.sim.distribution.GeometricServiceTimeDistribution;
import org.sim.distribution.NormalServiceTimeDistribution;
import org.sim.event.EventGenerator;
import org.sim.model.Clients;
import org.sim.stat.SimulationStatistics;
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
	SimulationStatistics provideStatisticsCollector(@NonNull final Clients clients) {
		return new SimulationStatistics(new LinkedList<>(), clients);
	}

	@Provides
	@Singleton
	ExponentialDistribution provideArrivalDistribution() {
		return new ExponentialDistribution(1.0 / Constants.CLIENT_ARRIVAL_RATE_PER_SECOND);
	}

	@Provides
	EventGenerator provideEventGenerator(ExponentialDistribution arrivalDistribution) {
		return new EventGenerator(arrivalDistribution);
	}

	@Provides
	Map<StationName, StationSpecification> provideStationSpecifications(
			@NonNull final SimulationStatistics simulationStatistics) {
		final StationSpecification cashierStationSpecification = new StationSpecification(
				new ExponentialServiceTimeDistribution(Constants.CASHIER_STATION_MEAN), new LinkedList<>(),
				simulationStatistics);
		final StationSpecification drinksStationSpecification = new StationSpecification(
				new ExponentialServiceTimeDistribution(Constants.DRINKS_STATION_MEAN), new LinkedList<>(),
				simulationStatistics);
		final StationSpecification frierStationSpecification = new StationSpecification(
				new NormalServiceTimeDistribution(Constants.FRIER_STATION_MEAN, Constants.FRIER_STATION_STD),
				new LinkedList<>(), simulationStatistics);
		final StationSpecification desertStationSpecification = new StationSpecification(
				new BinomialServiceTimeDistribution(Constants.DESERT_STATION_N, Constants.DESERT_STATION_P),
				new LinkedList<>(), simulationStatistics);
		final StationSpecification chickenStationSpecification = new StationSpecification(
				new GeometricServiceTimeDistribution(Constants.CHICKEN_STATION_P), new LinkedList<>(),
				simulationStatistics);

		return Map.of(
				StationName.CASHIER, cashierStationSpecification,
				StationName.DRINKS, drinksStationSpecification,
				StationName.FRIER, frierStationSpecification,
				StationName.DESERT, desertStationSpecification,
				StationName.CHICKEN, chickenStationSpecification);
	}

	@Provides
	@Singleton
	StationWorkflowGenerator provideStationWorkflowGenerator(
			@NonNull final Map<StationName, StationSpecification> stationSpecifications) {
		return new StationWorkflowGenerator(Constants.TOTAL_NUMBER_OF_WORKERS, stationSpecifications);
	}

	@Provides
	@Singleton
	CombinationTester provideCombinationTester(@NonNull final EventGenerator eventGenerator,
			@NonNull final StationWorkflowGenerator stationWorkflowGenerator) {
		return new CombinationTester(Constants.NUMBER_OF_SIMULATIONS_PER_COMBINATION,
				Constants.SIMULATION_TIME_IN_SECONDS, eventGenerator, stationWorkflowGenerator);
	}
}
