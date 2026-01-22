package org.sim.optimizer;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.sim.station.StationName;
import org.sim.station.StationPrice;

@Slf4j
@AllArgsConstructor(onConstructor_ = @Inject)
public class BudgetOptimizer {
	private final StationPrice stationPrice;

	public double calculateTotalCost(@NonNull final ImmutableMap<StationName, Integer> configuration) {
		final double cost = configuration.entrySet().stream()
				.mapToDouble(entry -> stationPrice.of(entry.getKey()) * entry.getValue())
				.sum();
		// log.info("Total cost: {}", cost);
		// return cost;
		return 0; // for now
	}
}
