package org.sim.optimizer.time;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.sim.generator.EventGenerator;
import org.sim.optimizer.OptimizerResult;
import org.sim.run.SimulationRunner;
import org.sim.stat.multiple.TestResult;
import org.sim.station.StationName;
import org.sim.station.StationPrice;
import org.sim.station.assignment.StationConfiguration;
import org.sim.station.assignment.StationSpecification;

import java.util.Comparator;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.List;

@Slf4j
@AllArgsConstructor(onConstructor_ = @Inject)
public class TimeOptimizer {
	private final Integer numberOfSimulations;
	private final Double simulationTime;
	private final EventGenerator eventGenerator;
	private final ImmutableMap<StationName, StationSpecification> stationSpecifications;
	private final StationPrice stationPrice;

	public OptimizerResult getTop3ConfigurationsForBudget(@NonNull final Double budget) {
		final StationName[] stations = StationName.values();
		final int[] maxCounts = new int[stations.length];
		final int[] current = new int[stations.length];

		// How many workers can be assigned to each station, for example if the price
		// per worker for station A is 100 and the budget is 500 then 5
		for (int i = 0; i < stations.length; i++) {
			maxCounts[i] = (int) (budget / stationPrice.of(stations[i]));
		}

		final List<TestResult> results = generateConfigurations(stations, maxCounts, current, 0, budget)
				.parallel()
				.map(this::runSimulation)
				.sorted(Comparator.comparingDouble(TestResult::averageWaitTime))
				.limit(3)
				.collect(Collectors.toList());

		final List<Double> optimizedTimes = results.stream().map(TestResult::averageWaitTime).toList();
		return new OptimizerResult(optimizedTimes, results);
	}

	private Stream<StationConfiguration> generateConfigurations(@NonNull final StationName[] stations,
			final int[] maxCounts,
			final int[] currentCounts, final int index, final double budget) {
		final double cost = cost(stations, currentCounts);

		if (cost > budget) {
			return Stream.empty();
		}
		if (index == stations.length) {
			return Stream.of(createConfig(stations, currentCounts));
		}

		return IntStream.rangeClosed(1, maxCounts[index])
				.boxed()
				.flatMap(count -> {
					currentCounts[index] = count;
					return generateConfigurations(stations, maxCounts, currentCounts.clone(), index + 1, budget);
				});
	}

	private double cost(@NonNull final StationName[] stations, final int[] counts) {
		return IntStream.range(0, stations.length)
				.mapToDouble(i -> counts[i] * stationPrice.of(stations[i]))
				.sum();
	}

	private StationConfiguration createConfig(@NonNull final StationName[] stations, final int[] counts) {
		val builder = ImmutableMap.<StationName, Integer>builder();
		for (int i = 0; i < stations.length; i++) {
			builder.put(stations[i], counts[i]);
		}
		return new StationConfiguration(builder.build(), stationSpecifications);
	}

	private TestResult runSimulation(@NonNull final StationConfiguration config) {
		return SimulationRunner.run(numberOfSimulations, simulationTime, eventGenerator, config).getResults();
	}
}
