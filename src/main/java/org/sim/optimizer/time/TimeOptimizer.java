package org.sim.optimizer.time;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.sim.generator.EventGenerator;
import org.sim.generator.WorkerCountGenerator;
import org.sim.run.SimulationRunner;
import org.sim.stat.multiple.TestResult;
import org.sim.station.StationName;
import org.sim.station.StationPrice;
import org.sim.station.assignment.StationConfiguration;
import org.sim.station.assignment.StationSpecification;

import java.util.concurrent.ExecutorService;

@Slf4j
@AllArgsConstructor(onConstructor_ = @Inject)
public class TimeOptimizer {
	private final int numberOfSimulations;
	private final double simulationTime;
	private final EventGenerator eventGenerator;
	private final WorkerCountGenerator workerCountGenerator;
	private final ImmutableMap<StationName, StationSpecification> stationSpecifications;
	private final ExecutorService executor;
	private final StationPrice stationPrice;

	public TestResult getBestConfigurationForBudget(@NonNull final Double budget) {
		TestResult bestResult = null;
		double bestAvgWaitTime = Double.MAX_VALUE;

		// Simple brute force - try all valid combinations
		int maxFryers = (int) (budget / stationPrice.of(StationName.FRYER));
		int maxCashiers = (int) (budget / stationPrice.of(StationName.CASHIER));
		int maxDrinks = (int) (budget / stationPrice.of(StationName.DRINKS));
		int maxGrills = (int) (budget / stationPrice.of(StationName.CHICKEN));

		for (int fryers = 1; fryers <= maxFryers; fryers++) {
			for (int cashiers = 1; cashiers <= maxCashiers; cashiers++) {
				for (int drinks = 1; drinks <= maxDrinks; drinks++) {
					for (int grills = 1; grills <= maxGrills; grills++) {

						double totalCost = fryers * stationPrice.of(StationName.FRYER) +
								cashiers * stationPrice.of(StationName.CASHIER) +
								drinks * stationPrice.of(StationName.DRINKS) +
								grills * stationPrice.of(StationName.CHICKEN);

						if (totalCost <= budget) {
							StationConfiguration config = new StationConfiguration(
									ImmutableMap.of(
											StationName.FRYER, fryers,
											StationName.CASHIER, cashiers,
											StationName.DRINKS, drinks,
											StationName.CHICKEN, grills),
									stationSpecifications);

							TestResult result = SimulationRunner
									.run(numberOfSimulations, simulationTime, eventGenerator, config).getResults();

							if (result.averageWaitTime() < bestAvgWaitTime) {
								bestAvgWaitTime = result.averageWaitTime();
								bestResult = result;
							}
						}
					}
				}
			}
		}

		return bestResult;
	}
}
