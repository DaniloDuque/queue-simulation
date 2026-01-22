package org.sim.station;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.NoSuchElementException;

@Singleton
@AllArgsConstructor(onConstructor_ = @Inject)
public class StationPrice {
	private final ImmutableMap<StationName, Double> prices;

	public Double of(@NonNull final StationName stationName) throws NoSuchElementException {
		final Double price = prices.get(stationName);
		if (price == null) {
			throw new NoSuchElementException();
		}
		return price;
	}
}
