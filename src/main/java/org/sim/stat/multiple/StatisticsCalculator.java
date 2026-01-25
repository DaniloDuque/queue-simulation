package org.sim.stat.multiple;

import com.google.common.collect.ImmutableMap;
import lombok.NonNull;
import org.sim.station.StationName;

import java.util.*;
import java.util.stream.Collectors;

public class StatisticsCalculator {

	public static DetailedStats calculateDetailedStats(@NonNull final Collection<Double> data) {
		if (data.isEmpty()) {
			return new DetailedStats(0, 0, 0, 0, 0, 0, 0, 0);
		}

		final List<Double> sorted = data.stream().sorted().collect(Collectors.toList());
		final int n = sorted.size();

		final double mean = data.stream().mapToDouble(Double::doubleValue).average().orElse(0);
		final double variance = data.stream().mapToDouble(x -> Math.pow(x - mean, 2)).sum() / n;

		final double median = getPercentile(sorted, 50);
		final int mode = (int) Math.round(getMostFrequent(data));

		final double q1 = getPercentile(sorted, 25);
		final double q3 = getPercentile(sorted, 75);
		final double p90 = getPercentile(sorted, 90);
		final double p95 = getPercentile(sorted, 95);
		final double p99 = getPercentile(sorted, 99);

		return new DetailedStats(median, variance, mode, q1, q3, p90, p95, p99);
	}

	private static double getPercentile(@NonNull final List<Double> sorted, final double percentile) {
		final int index = (int) Math.ceil(percentile / 100.0 * sorted.size()) - 1;
		return sorted.get(Math.max(0, Math.min(index, sorted.size() - 1)));
	}

	private static double getMostFrequent(@NonNull final Collection<Double> data) {
		final Map<Integer, Long> frequency = data.stream()
				.collect(Collectors.groupingBy(x -> (int) Math.round(x), Collectors.counting()));
		return frequency.entrySet().stream()
				.max(Map.Entry.comparingByValue())
				.map(Map.Entry::getKey)
				.orElse(0);
	}

	public static ImmutableMap<String, Double> calculateCovariances(
			@NonNull final Map<StationName, Collection<Double>> stationTimes) {
		final Map<String, Double> covariances = new HashMap<>();
		final StationName[] stations = StationName.values();

		for (int i = 0; i < stations.length; i++) {
			for (int j = i + 1; j < stations.length; j++) {
				final String key = stations[i] + "-" + stations[j];
				double cov = calculateCovariance(
						new ArrayList<>(stationTimes.getOrDefault(stations[i], Collections.emptyList())),
						new ArrayList<>(stationTimes.getOrDefault(stations[j], Collections.emptyList())));
				covariances.put(key, cov);
			}
		}

		return ImmutableMap.copyOf(covariances);
	}

	private static double calculateCovariance(@NonNull final List<Double> x, @NonNull final List<Double> y) {
		if (x.isEmpty() || y.isEmpty())
			return 0.0;

		final int minSize = Math.min(x.size(), y.size());
		final List<Double> x_0 = x.subList(0, minSize);
		final List<Double> y_0 = y.subList(0, minSize);

		final double meanX = x_0.stream().mapToDouble(Double::doubleValue).average().orElse(0);
		final double meanY = y_0.stream().mapToDouble(Double::doubleValue).average().orElse(0);

		double sum = 0;
		for (int i = 0; i < minSize; i++) {
			sum += (x.get(i) - meanX) * (y.get(i) - meanY);
		}

		return sum / minSize;
	}

	public record DetailedStats(
			double median, double variance, int mode,
			double q1, double q3, double p90, double p95, double p99) {
	}
}
