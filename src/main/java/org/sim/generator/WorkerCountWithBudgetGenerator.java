package org.sim.generator;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import org.sim.optimizer.BudgetOptimizer;
import org.sim.station.StationName;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

@AllArgsConstructor(onConstructor_ = @Inject)
public class WorkerCountWithBudgetGenerator implements Iterator<ImmutableMap<StationName, Integer>> {
	private final CompositionGenerator compositionGenerator;
	private final BudgetOptimizer budgetOptimizer;
	private final Double budget;

	@Override
	public boolean hasNext() {
		return compositionGenerator.hasNext();
	}

	@Override
	public ImmutableMap<StationName, Integer> next() {
		if (!compositionGenerator.hasNext()) {
			throw new NoSuchElementException();
		}

		final int[] composition = compositionGenerator.next();
		final Map<StationName, Integer> workerCountPerStation = new HashMap<>();
		int i = 0;
		for (final StationName stationName : StationName.values()) {
			workerCountPerStation.put(stationName, composition[i++]);
		}
		final ImmutableMap<StationName, Integer> result = ImmutableMap.copyOf(workerCountPerStation);
		if (budgetOptimizer.calculateTotalCost(result) <= budget)
			return result;

		if (!compositionGenerator.hasNext())
			throw new NoSuchElementException();

		return next();

	}
}
