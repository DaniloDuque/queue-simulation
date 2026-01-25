package org.sim.module;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimulationConfiguration {
	@Builder.Default
	private final double cashierStationMean = Constants.CASHIER_STATION_MEAN;

	@Builder.Default
	private final double drinksStationMean = Constants.DRINKS_STATION_MEAN;

	@Builder.Default
	private final double fryerStationMean = Constants.FRYER_STATION_MEAN;

	@Builder.Default
	private final double fryerStationStd = Constants.FRYER_STATION_STD;

	@Builder.Default
	private final double chickenStationP = Constants.CHICKEN_STATION_P;

	@Builder.Default
	private final double chickenProb = Constants.CHICKEN_PROB;

	@Builder.Default
	private final double clientArrivalRatePerSecond = Constants.CLIENT_ARRIVAL_RATE_PER_SECOND;

	@Builder.Default
	private final int numberOfSimulations = Constants.NUMBER_OF_SIMULATIONS_PER_COMBINATION;

	@Builder.Default
	private final double simulationTime = Constants.SIMULATION_TIME_IN_SECONDS;
}
